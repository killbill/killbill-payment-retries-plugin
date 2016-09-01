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

import java.util.Map;
import java.util.TreeMap;

import org.killbill.billing.payment.api.PaymentMethod;
import org.killbill.billing.payment.plugin.api.PaymentTransactionInfoPlugin;
import org.killbill.billing.plugin.api.PluginProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesComputer {

    private static final Logger logger = LoggerFactory.getLogger(RulesComputer.class);

    private final Map<String, Map<Integer, AuthorizationDeclineCode>> perPluginDeclineCodes = new TreeMap<String, Map<Integer, AuthorizationDeclineCode>>();

    public RulesComputer() {
        populateReverseLookup("killbill-braintree_blue", BraintreeAuthorizationDeclineCode.values());
        populateReverseLookup("killbill-cybersource", ChasePaymentechAuthorizationDeclineCode.values());
    }

    public AuthorizationDeclineCode lookupAuthorizationDeclineCode(final PaymentMethod paymentMethod, final PaymentTransactionInfoPlugin paymentTransactionInfoPlugin) {
        final Map<Integer, AuthorizationDeclineCode> declineCodes = perPluginDeclineCodes.get(paymentMethod.getPluginName());
        if (declineCodes == null) {
            logger.info("Payment retries plugin not configured for plugin='{}'", paymentMethod.getPluginName());
            return null;
        }

        final String processorResponse = PluginProperties.getValue("processorResponse", paymentTransactionInfoPlugin.getGatewayErrorCode(), paymentTransactionInfoPlugin.getProperties());
        if (processorResponse == null) {
            logger.info("No processorResponseCode available from paymentTransactionInfoPlugin='{}'", paymentTransactionInfoPlugin);
            return null;
        }

        final Integer processorResponseCode;
        try {
            processorResponseCode = Integer.valueOf(processorResponse);
        } catch (final NumberFormatException e) {
            logger.info("Unable to extract processorResponseCode from processorResponse='{}'", processorResponse);
            return null;
        }

        return declineCodes.get(processorResponseCode);
    }

    public Map<String, Map<Integer, AuthorizationDeclineCode>> getPerPluginDeclineCodes() {
        return perPluginDeclineCodes;
    }

    private void populateReverseLookup(final String pluginName, final AuthorizationDeclineCode[] authorizationDeclineCodes) {
        final Map<Integer, AuthorizationDeclineCode> pluginDeclineCodes = new TreeMap<Integer, AuthorizationDeclineCode>();
        for (final AuthorizationDeclineCode authorizationDeclineCode : authorizationDeclineCodes) {
            pluginDeclineCodes.put(authorizationDeclineCode.getCode(), authorizationDeclineCode);
        }
        perPluginDeclineCodes.put(pluginName, pluginDeclineCodes);
    }
}
