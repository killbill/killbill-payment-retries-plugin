/*
 * Copyright 2016 Groupon, Inc
 * Copyright 2016 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.payment.retries;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIServiceNotAvailable;
import org.killbill.billing.payment.api.Payment;
import org.killbill.billing.payment.api.PaymentApi;
import org.killbill.billing.payment.api.PaymentApiException;
import org.killbill.billing.payment.api.PaymentMethod;
import org.killbill.billing.payment.api.PaymentTransaction;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.api.TransactionStatus;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.plugin.api.PluginTenantContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class OSGIKillbillAPIWrapper {

    private static final Logger logger = LoggerFactory.getLogger(OSGIKillbillAPIWrapper.class);

    private final OSGIKillbillAPI killbillAPI;

    public OSGIKillbillAPIWrapper(final OSGIKillbillAPI killbillAPI) {
        this.killbillAPI = killbillAPI;
    }

    public Payment getPayment(final String paymentExternalKey, final UUID tenantId) {
        final TenantContext tenantContext = new PluginTenantContext(tenantId);

        try {
            final PaymentApi paymentApi = killbillAPI.getPaymentApi();
            return paymentApi.getPaymentByExternalKey(paymentExternalKey,
                                                      true,
                                                      false,
                                                      ImmutableList.<PluginProperty>of(),
                                                      tenantContext);
        } catch (final OSGIServiceNotAvailable e) {
            logger.warn("Unable to retrieve payment for paymentExternalKey='{}'", paymentExternalKey, e);
            return null;
        } catch (final PaymentApiException e) {
            logger.warn("Unable to retrieve payment for paymentExternalKey='{}'", paymentExternalKey, e);
            return null;
        }
    }

    public PaymentMethod getPaymentMethod(final UUID paymentMethodId, final UUID tenantId) {
        final TenantContext tenantContext = new PluginTenantContext(tenantId);

        try {
            final PaymentApi paymentApi = killbillAPI.getPaymentApi();
            return paymentApi.getPaymentMethodById(paymentMethodId,
                                                   false,
                                                   false,
                                                   ImmutableList.<PluginProperty>of(),
                                                   tenantContext);
        } catch (final OSGIServiceNotAvailable e) {
            logger.warn("Unable to retrieve payment method for paymentMethodId='{}'", paymentMethodId, e);
            return null;
        } catch (final PaymentApiException e) {
            logger.warn("Unable to retrieve payment method for paymentMethodId='{}'", paymentMethodId, e);
            return null;
        }
    }

    public PaymentTransaction getLastAuthorizationIfFailed(final UUID accountId, final UUID paymentMethodId, final UUID tenantId) {
        final TenantContext tenantContext = new PluginTenantContext(tenantId);

        try {
            final PaymentApi paymentApi = killbillAPI.getPaymentApi();
            final List<Payment> payments = paymentApi.getAccountPayments(accountId,
                                                                         true,
                                                                         false,
                                                                         ImmutableList.<PluginProperty>of(),
                                                                         tenantContext);
            return getLastAuthorizationIfFailed(payments, paymentMethodId);
        } catch (final OSGIServiceNotAvailable e) {
            logger.warn("Unable to retrieve payments for accountId='{}'", accountId, e);
            return null;
        } catch (final PaymentApiException e) {
            logger.warn("Unable to retrieve payments for accountId='{}'", accountId, e);
            return null;
        }
    }

    public PaymentTransaction getLastAuthorizationIfFailed(final Payment payment) {
        return getLastAuthorizationIfFailed(ImmutableList.<Payment>of(payment), payment.getPaymentMethodId());
    }

    @VisibleForTesting
    PaymentTransaction getLastAuthorizationIfFailed(final Collection<Payment> payments, final UUID paymentMethodId) {
        if (payments.isEmpty()) {
            // First payment for this account
            return null;
        }

        final Collection<Payment> paymentsForCurrentPaymentMethod = Collections2.<Payment>filter(payments,
                                                                                                 new Predicate<Payment>() {
                                                                                                     @Override
                                                                                                     public boolean apply(final Payment payment) {
                                                                                                         return paymentMethodId.equals(payment.getPaymentMethodId());
                                                                                                     }
                                                                                                 });
        if (paymentsForCurrentPaymentMethod.isEmpty()) {
            // First payment for this payment method
            return null;
        }

        final Payment lastPayment = Iterables.getLast(paymentsForCurrentPaymentMethod);
        // Non-zero auth or auth voided means the authorization was successful
        if ((lastPayment.getAuthAmount() != null && BigDecimal.ZERO.compareTo(lastPayment.getAuthAmount()) != 0) ||
            lastPayment.isAuthVoided()) {
            return null;
        }

        final Collection<PaymentTransaction> failedAuthorizations = Collections2.<PaymentTransaction>filter(lastPayment.getTransactions(),
                                                                                                            new Predicate<PaymentTransaction>() {
                                                                                                                @Override
                                                                                                                public boolean apply(final PaymentTransaction paymentTransaction) {
                                                                                                                    return paymentTransaction.getTransactionType() == TransactionType.AUTHORIZE ||
                                                                                                                           paymentTransaction.getTransactionStatus() == TransactionStatus.PAYMENT_FAILURE;
                                                                                                                }
                                                                                                            });
        if (failedAuthorizations.isEmpty()) {
            return null;
        } else {
            return Iterables.getLast(failedAuthorizations);
        }
    }
}
