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

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.killbill.billing.payment.api.PaymentMethod;
import org.killbill.billing.payment.plugin.api.PaymentTransactionInfoPlugin;
import org.killbill.billing.plugin.api.PluginProperties;
import org.killbill.billing.plugin.payment.retries.api.AuthorizationDeclineCode;
import org.killbill.billing.plugin.payment.retries.api.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

public class RulesComputer {

    private final static String ADYEN_PLUGIN = "killbill-adyen";
    private final static String BRAINTREE_BLUE_PLUGIN = "killbill-braintree_blue";
    private final static String ORBITAL = "killbill-orbital";
    private final static String CYBERSOURCE = "killbill-cybersource";

    final static String PAYMENTECH = "Paymentech";
    final static String BRAINTREE = "Braintree";
    final static String AYDEN_PROCESSOR = "Adyen";

    private final static int DEFAULT_ERROR_CODE = 0;
    private final static Logger logger = LoggerFactory.getLogger(RulesComputer.class);

    public final static Map<String, String> PROCESSOR_PER_GATEWAY = ImmutableMap.of(ADYEN_PLUGIN, AYDEN_PROCESSOR,
                                                                                    BRAINTREE_BLUE_PLUGIN, BRAINTREE,
                                                                                    ORBITAL, PAYMENTECH,
                                                                                    CYBERSOURCE, PAYMENTECH);

    private final Map<String, Map<Integer, AuthorizationDeclineCode>> perPluginDeclineCodes = new TreeMap<>();

    public RulesComputer() {
        populateReverseLookup(ADYEN_PLUGIN, AdyenAuthorizationDeclineCode.values());
        populateReverseLookup(BRAINTREE_BLUE_PLUGIN, BraintreeAuthorizationDeclineCode.values());
        populateReverseLookup(CYBERSOURCE, ChasePaymentechAuthorizationDeclineCode.values());
        populateReverseLookup(ORBITAL, ChasePaymentechAuthorizationDeclineCode.values());
    }

    public AuthorizationDeclineCode lookupAuthorizationDeclineCode(final PaymentMethod paymentMethod, final PaymentTransactionInfoPlugin paymentTransactionInfoPlugin) {
        final Map<Integer, AuthorizationDeclineCode> declineCodes = perPluginDeclineCodes.get(paymentMethod.getPluginName());
        if (declineCodes == null) {
            logger.info("Payment retries plugin not configured for plugin='{}'", paymentMethod.getPluginName());
            return null;
        }

        final String processorResponse = PluginProperties.getValue("processorResponse", paymentTransactionInfoPlugin.getGatewayErrorCode(), paymentTransactionInfoPlugin.getProperties());
        final Integer processorResponseCode;
        try {
            processorResponseCode = Integer.valueOf(processorResponse);
            return declineCodes.get(processorResponseCode);
        } catch (final NumberFormatException e) {
            logger.info("Unable to extract an integer processorResponseCode from processorResponse='{}'. " +
                        "Trying fuzzy match based on error message.", processorResponse);
            return getFuzzyMatchedAuthDeclineCode(paymentTransactionInfoPlugin.getGatewayError(), paymentMethod.getPluginName());
        }
    }

    private AuthorizationDeclineCode getFuzzyMatchedAuthDeclineCode(final String gatewayError, final String paymentMethodPlugin) {
        ErrorMessage errorMessage = getFuzzyMatchedErrorMessage(gatewayError);
        if(errorMessage != null) {
            return new DefaultAuthorizationDeclineCode(PROCESSOR_PER_GATEWAY.get(paymentMethodPlugin),
                                                       DEFAULT_ERROR_CODE,
                                                       gatewayError,
                                                       errorMessage,
                                                       errorMessage.isRetryable());
        } else {
            logger.info("Unable to fuzzy match an error category based on the error message='{}'.", gatewayError);
            return null;
        }
    }

    private ErrorMessage getFuzzyMatchedErrorMessage(final String gatewayError) {
        return Arrays.stream(ErrorMessage.values())
                     .filter(error -> error.test(gatewayError))
                     .findFirst()
                     .orElse(null);
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
