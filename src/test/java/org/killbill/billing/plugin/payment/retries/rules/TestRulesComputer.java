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

package org.killbill.billing.plugin.payment.retries.rules;

import java.util.UUID;

import org.killbill.billing.payment.api.PaymentMethod;
import org.killbill.billing.payment.plugin.api.PaymentTransactionInfoPlugin;
import org.killbill.billing.plugin.TestUtils;
import org.killbill.billing.plugin.payment.retries.api.AuthorizationDeclineCode;
import org.killbill.billing.plugin.payment.retries.api.ErrorMessage;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestRulesComputer {

    @Test(groups = "fast")
    public void testLookupAuthorizationDeclineCode() throws Exception {
        final RulesComputer rulesComputer = new RulesComputer();
        final UUID accountId = UUID.randomUUID();
        final PaymentMethod otherPm = TestUtils.buildPaymentMethod(accountId, UUID.randomUUID(), UUID.randomUUID().toString());

        final PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = Mockito.mock(PaymentTransactionInfoPlugin.class);
        Mockito.when(paymentTransactionInfoPlugin.getSecondPaymentReferenceId()).thenReturn("250");
        Assert.assertNull(rulesComputer.lookupAuthorizationDeclineCode(otherPm, paymentTransactionInfoPlugin));
    }

    @Test(groups = "fast")
    public void testLookupAuthorizationDeclineCodeWithIntegerErrorCode() throws Exception {
        final RulesComputer rulesComputer = new RulesComputer();
        final PaymentMethod pm = TestUtils.buildPaymentMethod(UUID.randomUUID(), UUID.randomUUID(), "killbill-adyen");

        final PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = Mockito.mock(PaymentTransactionInfoPlugin.class);
        Mockito.when(paymentTransactionInfoPlugin.getGatewayErrorCode()).thenReturn("62");
        AuthorizationDeclineCode code = rulesComputer.lookupAuthorizationDeclineCode(pm, paymentTransactionInfoPlugin);
        Assert.assertEquals(code.getErrorMessage(), ErrorMessage.GENERAL_DECLINE);
        Assert.assertEquals(code.getMessage(), "Restricted card");
        Assert.assertEquals(code.getCode(), 62);
        Assert.assertEquals(code.isRetryable(), true);
    }

    @Test(groups = "fast")
    public void testLookupAuthorizationDeclineCodeWithFuzzyMatch() throws Exception {
        final RulesComputer rulesComputer = new RulesComputer();
        final PaymentMethod pm = TestUtils.buildPaymentMethod(UUID.randomUUID(), UUID.randomUUID(), "killbill-adyen");

        final PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = Mockito.mock(PaymentTransactionInfoPlugin.class);
        Mockito.when(paymentTransactionInfoPlugin.getGatewayError()).thenReturn("CVC Declined");
        AuthorizationDeclineCode code = rulesComputer.lookupAuthorizationDeclineCode(pm, paymentTransactionInfoPlugin);
        Assert.assertEquals(code.getErrorMessage(), ErrorMessage.CVV_MISMATCH);
        Assert.assertEquals(code.getMessage(), "CVC Declined");
        Assert.assertEquals(code.getCode(), 0);
        Assert.assertEquals(code.isRetryable(), false);
    }

    @Test(groups = "fast")
    public void testLookupAuthorizationDeclineCodeFuzzyMatchFailed() throws Exception {
        final RulesComputer rulesComputer = new RulesComputer();
        final PaymentMethod pm = TestUtils.buildPaymentMethod(UUID.randomUUID(), UUID.randomUUID(), "killbill-adyen");

        final PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = Mockito.mock(PaymentTransactionInfoPlugin.class);
        Mockito.when(paymentTransactionInfoPlugin.getGatewayError()).thenReturn("Card blocked.");
        Assert.assertNull(rulesComputer.lookupAuthorizationDeclineCode(pm, paymentTransactionInfoPlugin));
    }
}
