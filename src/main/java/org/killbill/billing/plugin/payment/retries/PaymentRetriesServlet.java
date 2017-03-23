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

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.killbill.billing.plugin.payment.retries.api.PaymentRetriesApi;
import org.killbill.billing.plugin.payment.retries.api.AuthorizationDeclineCode;
import org.killbill.billing.tenant.api.Tenant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

public class PaymentRetriesServlet extends HttpServlet {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static final String KILLBILL_TENANT = "killbill_tenant";

    private static final Pattern PAYMENT_METHOD_CHECK_PATTERN = Pattern.compile("/paymentMethodCheck");
    private static final Pattern CONFIGURATION_PATTERN = Pattern.compile("/configuration");

    private static final String PAYMENT_EXTERNAL_KEY = "paymentExternalKey";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String RETRYABLE = "retryable";

    private static final String APPLICATION_JSON = "application/json";

    private final PaymentRetriesApi paymentRetriesApi;

    public PaymentRetriesServlet(final PaymentRetriesApi paymentRetriesApi) {
        this.paymentRetriesApi = paymentRetriesApi;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String pathInfo = req.getPathInfo();

        if (PAYMENT_METHOD_CHECK_PATTERN.matcher(pathInfo).matches()) {
            final String paymentExternalKey = req.getParameter(PAYMENT_EXTERNAL_KEY);

            // Set by the TenantFilter
            final Tenant tenant = (Tenant) req.getAttribute(KILLBILL_TENANT);
            if (tenant == null) {
                resp.sendError(404);
                return;
            }

            final AuthorizationDeclineCode authorizationDeclineCode = paymentRetriesApi.getAuthorizationDeclineCode(paymentExternalKey, tenant.getId());
            if (authorizationDeclineCode == null) {
                resp.sendError(404);
            } else {
                resp.getOutputStream().write(jsonMapper.writeValueAsBytes(authorizationDeclineCode));
                resp.setContentType(APPLICATION_JSON);
            }
        } else if (CONFIGURATION_PATTERN.matcher(pathInfo).matches()) {
            final boolean retryable = Boolean.valueOf(req.getParameter(RETRYABLE));
            final String errorMessage = req.getParameter(ERROR_MESSAGE);

            final Map<String, Map<Integer, AuthorizationDeclineCode>> perPluginDeclineCodes = paymentRetriesApi.getPerPluginDeclineCodes();
            final Map<String, Map<Integer, AuthorizationDeclineCode>> returnedMap = new TreeMap<String, Map<Integer, AuthorizationDeclineCode>>();
            for (final String pluginName : perPluginDeclineCodes.keySet()) {
                returnedMap.put(pluginName,
                                Maps.<Integer, AuthorizationDeclineCode>filterValues(perPluginDeclineCodes.get(pluginName),
                                                                                     new Predicate<AuthorizationDeclineCode>() {
                                                                                         @Override
                                                                                         public boolean apply(final AuthorizationDeclineCode authorizationDeclineCode) {
                                                                                             return retryable == authorizationDeclineCode.isRetryable() &&
                                                                                                    (errorMessage == null || authorizationDeclineCode.getErrorMessage().name().equalsIgnoreCase(errorMessage));
                                                                                         }
                                                                                     }));
            }

            resp.getOutputStream().write(jsonMapper.writeValueAsBytes(returnedMap));
            resp.setContentType(APPLICATION_JSON);
        } else {
            resp.sendError(404);
        }
    }
}
