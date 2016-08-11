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
import java.util.UUID;

import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.payment.api.Payment;
import org.killbill.billing.payment.api.PaymentTransaction;
import org.killbill.billing.payment.api.TransactionStatus;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.plugin.TestUtils;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

public class TestOSGIKillbillAPIWrapper {

    @Test(groups = "fast")
    public void testGetLastAuthorizationIfFailed() throws Exception {
        final OSGIKillbillAPIWrapper osgiKillbillAPIWrapper = new OSGIKillbillAPIWrapper(null);

        final UUID accountId = UUID.randomUUID();
        final UUID paymentMethodId = UUID.randomUUID();

        final Payment paymentOtherPaymentMethod = TestUtils.buildPayment(accountId, UUID.randomUUID(), Currency.USD);

        final Payment successfulPayment = TestUtils.buildPayment(accountId, paymentMethodId, Currency.USD);
        Mockito.when(successfulPayment.getAuthAmount()).thenReturn(BigDecimal.TEN);
        TestUtils.buildPaymentTransaction(successfulPayment, TransactionType.AUTHORIZE, TransactionStatus.PAYMENT_FAILURE, BigDecimal.TEN, Currency.USD);
        TestUtils.buildPaymentTransaction(successfulPayment, TransactionType.AUTHORIZE, TransactionStatus.SUCCESS, BigDecimal.TEN, Currency.USD);

        final Payment failedPayment = TestUtils.buildPayment(accountId, paymentMethodId, Currency.USD);
        final PaymentTransaction expectedPaymentTransaction = TestUtils.buildPaymentTransaction(failedPayment, TransactionType.AUTHORIZE, TransactionStatus.PAYMENT_FAILURE, BigDecimal.TEN, Currency.USD);

        Assert.assertNull(osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(ImmutableList.<Payment>of(failedPayment), UUID.randomUUID()));
        Assert.assertNull(osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(ImmutableList.<Payment>of(), paymentMethodId));
        Assert.assertNull(osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(ImmutableList.<Payment>of(paymentOtherPaymentMethod, failedPayment, successfulPayment), paymentMethodId));

        Assert.assertEquals(osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(ImmutableList.<Payment>of(failedPayment, paymentOtherPaymentMethod), paymentMethodId), expectedPaymentTransaction);
        Assert.assertEquals(osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(ImmutableList.<Payment>of(paymentOtherPaymentMethod, failedPayment), paymentMethodId), expectedPaymentTransaction);
        Assert.assertEquals(osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(ImmutableList.<Payment>of(successfulPayment, paymentOtherPaymentMethod, failedPayment), paymentMethodId), expectedPaymentTransaction);
        Assert.assertEquals(osgiKillbillAPIWrapper.getLastAuthorizationIfFailed(ImmutableList.<Payment>of(paymentOtherPaymentMethod, successfulPayment, failedPayment), paymentMethodId), expectedPaymentTransaction);
    }
}
