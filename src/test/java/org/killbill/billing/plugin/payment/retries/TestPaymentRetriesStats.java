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

import org.killbill.billing.plugin.payment.retries.rules.BraintreeAuthorizationDeclineCode;
import org.killbill.billing.plugin.payment.retries.rules.ChasePaymentechAuthorizationDeclineCode;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPaymentRetriesStats {

    @Test(groups = "fast")
    public void testStats() throws Exception {
        final PaymentRetriesStats stats = new PaymentRetriesStats();
        Assert.assertEquals(stats.getNbOfRetries(ChasePaymentechAuthorizationDeclineCode.INVALID_INSTITUTION), 0);

        Assert.assertEquals(stats.getNbOfRetries(BraintreeAuthorizationDeclineCode.INVALID_AUTHORIZATION_CODE), 0);
        Assert.assertEquals(stats.markRetriedPaymentAsFailed(BraintreeAuthorizationDeclineCode.INVALID_AUTHORIZATION_CODE), 1);
        Assert.assertEquals(stats.getNbOfRetries(BraintreeAuthorizationDeclineCode.INVALID_AUTHORIZATION_CODE), 1);
        Assert.assertEquals(stats.markRetriedPaymentAsSuccessful(BraintreeAuthorizationDeclineCode.INVALID_AUTHORIZATION_CODE), 1);
        Assert.assertEquals(stats.getNbOfRetries(BraintreeAuthorizationDeclineCode.INVALID_AUTHORIZATION_CODE), 2);

        Assert.assertEquals(stats.getNbOfRetries(ChasePaymentechAuthorizationDeclineCode.INVALID_INSTITUTION), 0);
    }
}
