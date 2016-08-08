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

import java.util.Properties;

import org.killbill.billing.control.plugin.api.OnFailurePaymentControlResult;
import org.killbill.billing.control.plugin.api.OnSuccessPaymentControlResult;
import org.killbill.billing.control.plugin.api.PaymentControlApiException;
import org.killbill.billing.control.plugin.api.PaymentControlContext;
import org.killbill.billing.control.plugin.api.PaymentControlPluginApi;
import org.killbill.billing.control.plugin.api.PriorPaymentControlResult;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.payment.api.PluginProperty;

public class PaymentRetriesPaymentControlPluginApi implements PaymentControlPluginApi {

    public PaymentRetriesPaymentControlPluginApi(final Properties properties, final OSGIKillbillLogService logService) {
    }

    @Override
    public PriorPaymentControlResult priorCall(final PaymentControlContext context, final Iterable<PluginProperty> properties) throws PaymentControlApiException {
        return null;
    }

    @Override
    public OnSuccessPaymentControlResult onSuccessCall(final PaymentControlContext context, final Iterable<PluginProperty> properties) throws PaymentControlApiException {
        return null;
    }

    @Override
    public OnFailurePaymentControlResult onFailureCall(final PaymentControlContext context, final Iterable<PluginProperty> properties) throws PaymentControlApiException {
        return null;
    }
}
