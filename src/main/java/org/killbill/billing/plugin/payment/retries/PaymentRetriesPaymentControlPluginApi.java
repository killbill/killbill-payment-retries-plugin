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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.killbill.billing.control.plugin.api.OnFailurePaymentControlResult;
import org.killbill.billing.control.plugin.api.OnSuccessPaymentControlResult;
import org.killbill.billing.control.plugin.api.PaymentControlApiException;
import org.killbill.billing.control.plugin.api.PaymentControlContext;
import org.killbill.billing.control.plugin.api.PriorPaymentControlResult;
import org.killbill.billing.osgi.libs.killbill.OSGIConfigPropertiesService;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.api.control.PluginPaymentControlPluginApi;
import org.killbill.billing.plugin.api.control.PluginPriorPaymentControlResult;
import org.killbill.billing.plugin.payment.retries.api.PaymentRetriesApi;
import org.killbill.billing.plugin.payment.retries.config.PaymentRetriesConfiguration;
import org.killbill.billing.plugin.payment.retries.config.PaymentRetriesConfigurationHandler;
import org.killbill.billing.plugin.payment.retries.api.AuthorizationDeclineCode;
import org.killbill.clock.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentRetriesPaymentControlPluginApi extends PluginPaymentControlPluginApi {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRetriesPaymentControlPluginApi.class);

    private final Map<UUID, AuthorizationDeclineCode> controlGroup = new ConcurrentHashMap<UUID, AuthorizationDeclineCode>();
    private final AtomicLong nbOfRetries = new AtomicLong(0L);
    private final AtomicLong nbOfAbortedRetries = new AtomicLong(0L);

    private final PaymentRetriesStats paymentRetriesStats = new PaymentRetriesStats();

    private final PaymentRetriesConfigurationHandler paymentRetriesConfigurationHandler;
    private final PaymentRetriesApi paymentRetriesApi;

    public PaymentRetriesPaymentControlPluginApi(final PaymentRetriesConfigurationHandler paymentRetriesConfigurationHandler,
                                                 final PaymentRetriesApi paymentRetriesApi,
                                                 final OSGIKillbillAPI killbillAPI,
                                                 final OSGIConfigPropertiesService configProperties,
                                                 final OSGIKillbillLogService logService,
                                                 final Clock clock) {
        super(killbillAPI, configProperties, logService, clock);
        this.paymentRetriesConfigurationHandler = paymentRetriesConfigurationHandler;
        this.paymentRetriesApi = paymentRetriesApi;
    }

    @Override
    public PriorPaymentControlResult priorCall(final PaymentControlContext context, final Iterable<PluginProperty> properties) throws PaymentControlApiException {
        final AuthorizationDeclineCode authorizationDeclineCode = paymentRetriesApi.getAuthorizationDeclineCode(context.getAccountId(), context.getPaymentMethodId(), context.getTenantId());
        if (authorizationDeclineCode == null) {
            return super.priorCall(context, properties);
        }

        final boolean isAborted;
        if (shouldBeFiltered(context.getTenantId())) {
            isAborted = authorizationDeclineCode.isRetryable();
        } else {
            // Gradual ramp-up to verify the rules
            isAborted = false;
            controlGroup.put(context.getAttemptPaymentId(), authorizationDeclineCode);
        }

        // Log the stats from the Experiment Group to monitor the rules
        final long nbOfAttemptedRetries = nbOfRetries.incrementAndGet();
        if (isAborted) {
            final Long totalAborted = nbOfAbortedRetries.incrementAndGet();
            final double totalAbortedPct = Math.floor(totalAborted / nbOfAttemptedRetries * 100.0);
            logger.info("Prevented retry attemptPaymentId='{}', totalAborted='{}', nbOfAttemptedRetries='{}', totalAbortedPct='{}', processorMessage='{}', processorCode='{}', isRetryable='{}'",
                        context.getAttemptPaymentId(), totalAborted, nbOfAttemptedRetries, totalAbortedPct, authorizationDeclineCode.getMessage(), authorizationDeclineCode.getCode(), authorizationDeclineCode.isRetryable());
        }

        return new PluginPriorPaymentControlResult(isAborted, context);
    }

    @Override
    public OnSuccessPaymentControlResult onSuccessCall(final PaymentControlContext context, final Iterable<PluginProperty> properties) throws PaymentControlApiException {
        final AuthorizationDeclineCode authorizationDeclineCode = controlGroup.remove(context.getAttemptPaymentId());
        if (authorizationDeclineCode != null) {
            // Log the stats from the Control Group to tweak the rules
            final long nbOfSuccessfulRetries = paymentRetriesStats.markRetriedPaymentAsSuccessful(authorizationDeclineCode);
            final long nbOfRetries = paymentRetriesStats.getNbOfRetries(authorizationDeclineCode);
            final double totalSuccessesPct = Math.floor(nbOfSuccessfulRetries / nbOfRetries * 100.0);
            logger.info("Successful retry attemptPaymentId='{}', nbOfSuccessfulRetries='{}', nbOfRetries='{}', totalSuccessesPct='{}', processorMessage='{}', processorCode='{}', isRetryable='{}'",
                        context.getAttemptPaymentId(), nbOfSuccessfulRetries, nbOfRetries, totalSuccessesPct, authorizationDeclineCode.getMessage(), authorizationDeclineCode.getCode(), authorizationDeclineCode.isRetryable());
        }

        return super.onSuccessCall(context, properties);
    }

    @Override
    public OnFailurePaymentControlResult onFailureCall(final PaymentControlContext context, final Iterable<PluginProperty> properties) throws PaymentControlApiException {
        final AuthorizationDeclineCode authorizationDeclineCode = controlGroup.remove(context.getAttemptPaymentId());
        if (authorizationDeclineCode != null) {
            // Log the stats from the Control Group to tweak the rules
            final long nbOfFailedRetries = paymentRetriesStats.markRetriedPaymentAsFailed(authorizationDeclineCode);
            final long nbOfRetries = paymentRetriesStats.getNbOfRetries(authorizationDeclineCode);
            final double totalFailedPct = Math.floor(nbOfFailedRetries / nbOfRetries * 100.0);
            logger.info("Failed retry attemptPaymentId='{}', nbOfFailedRetries='{}', nbOfRetries='{}', totalFailedPct='{}', processorMessage='{}', processorCode='{}', isRetryable='{}'",
                        context.getAttemptPaymentId(), nbOfFailedRetries, nbOfRetries, totalFailedPct, authorizationDeclineCode.getMessage(), authorizationDeclineCode.getCode(), authorizationDeclineCode.isRetryable());
        }

        return super.onFailureCall(context, properties);
    }

    private boolean shouldBeFiltered(final UUID tenantId) {
        final PaymentRetriesConfiguration paymentRetriesConfiguration = paymentRetriesConfigurationHandler.getConfigurable(tenantId);
        return Math.abs(ThreadLocalRandom.current().nextInt()) % 100 < paymentRetriesConfiguration.getExperimentTrafficPct();
    }
}
