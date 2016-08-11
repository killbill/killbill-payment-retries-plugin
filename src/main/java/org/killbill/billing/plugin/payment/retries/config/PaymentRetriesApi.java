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

package org.killbill.billing.plugin.payment.retries.config;

import java.util.UUID;

import javax.annotation.Nullable;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.payment.api.Payment;
import org.killbill.billing.payment.api.PaymentMethod;
import org.killbill.billing.payment.api.PaymentTransaction;
import org.killbill.billing.plugin.payment.retries.OSGIKillbillAPIWrapper;
import org.killbill.billing.plugin.payment.retries.rules.AuthorizationDeclineCode;
import org.killbill.billing.plugin.payment.retries.rules.RulesComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentRetriesApi {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRetriesApi.class);

    private final RulesComputer rulesComputer = new RulesComputer();
    private final OSGIKillbillAPIWrapper osgiKillbillAPIWrapper;

    public PaymentRetriesApi(final OSGIKillbillAPI killbillAPI) {
        this.osgiKillbillAPIWrapper = new OSGIKillbillAPIWrapper(killbillAPI);
    }

    public AuthorizationDeclineCode getAuthorizationDeclineCode(final UUID accountId, final UUID paymentMethodId, final UUID tenantId) {
        final PaymentTransaction failedAuthorization = osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(accountId, paymentMethodId, tenantId);
        return getAuthorizationDeclineCode(failedAuthorization, paymentMethodId, tenantId);
    }

    public AuthorizationDeclineCode getAuthorizationDeclineCode(final String paymentExternalKey, final UUID tenantId) {
        final Payment payment = osgiKillbillAPIWrapper.getPayment(paymentExternalKey, tenantId);
        final PaymentTransaction failedAuthorization = osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(payment);
        return getAuthorizationDeclineCode(failedAuthorization, payment.getPaymentMethodId(), tenantId);
    }

    private AuthorizationDeclineCode getAuthorizationDeclineCode(@Nullable final PaymentTransaction failedAuthorization, final UUID paymentMethodId, final UUID tenantId) {
        if (failedAuthorization == null || failedAuthorization.getPaymentInfoPlugin() == null) {
            // Last payment was successful -- the payment method is most likely still valid
            return null;
        }

        // If the last payment wasn't successful, check if the failure was temporary
        final PaymentMethod paymentMethod = osgiKillbillAPIWrapper.getPaymentMethod(paymentMethodId, tenantId);
        final AuthorizationDeclineCode authorizationDeclineCode = rulesComputer.lookupAuthorizationDeclineCode(paymentMethod, failedAuthorization.getPaymentInfoPlugin());
        if (authorizationDeclineCode == null) {
            return null;
        } else {
            logger.info("PaymentRetriesApi paymentTransactionId='{}', paymentTransactionExternalKey='{}', gatewayErrorCode='{}', gatewayErrorMsg='{}', processorMessage='{}', processorCode='{}', isRetryable='{}'",
                        failedAuthorization.getId(), failedAuthorization.getExternalKey(), failedAuthorization.getGatewayErrorCode(), failedAuthorization.getGatewayErrorMsg(),
                        authorizationDeclineCode.getMessage(), authorizationDeclineCode.getCode(), authorizationDeclineCode.isRetryable());
            return authorizationDeclineCode;
        }
    }
}
