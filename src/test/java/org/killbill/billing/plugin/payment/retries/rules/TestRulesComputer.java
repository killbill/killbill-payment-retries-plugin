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
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestRulesComputer {

    @Test(groups = "fast")
    public void testLookupAuthorizationDeclineCode() throws Exception {
        final RulesComputer rulesComputer = new RulesComputer();
        final UUID accountId = UUID.randomUUID();
        final PaymentMethod otherPm = TestUtils.buildPaymentMethod(accountId, UUID.randomUUID(), UUID.randomUUID().toString());
        final PaymentMethod csPm = TestUtils.buildPaymentMethod(accountId, UUID.randomUUID(), "killbill-cybersource");

        final PaymentTransactionInfoPlugin paymentTransactionInfoPlugin = Mockito.mock(PaymentTransactionInfoPlugin.class);
        Mockito.when(paymentTransactionInfoPlugin.getSecondPaymentReferenceId()).thenReturn("250");
        Assert.assertNull(rulesComputer.lookupAuthorizationDeclineCode(otherPm, paymentTransactionInfoPlugin));
    }
}
