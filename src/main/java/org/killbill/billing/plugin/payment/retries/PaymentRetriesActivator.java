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

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.killbill.billing.control.plugin.api.PaymentControlPluginApi;
import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.killbill.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.plugin.api.notification.PluginConfigurationEventHandler;
import org.killbill.billing.plugin.payment.retries.config.PaymentRetriesApi;
import org.killbill.billing.plugin.payment.retries.config.PaymentRetriesConfiguration;
import org.killbill.billing.plugin.payment.retries.config.PaymentRetriesConfigurationHandler;
import org.osgi.framework.BundleContext;

public class PaymentRetriesActivator extends KillbillActivatorBase {

    public static final String PLUGIN_NAME = "payment-retries-plugin";

    private PaymentRetriesConfigurationHandler paymentRetriesConfigurationHandler;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        paymentRetriesConfigurationHandler = new PaymentRetriesConfigurationHandler(PLUGIN_NAME, killbillAPI, logService);

        final PaymentRetriesConfiguration globalConfigurable = paymentRetriesConfigurationHandler.createConfigurable(configProperties.getProperties());
        paymentRetriesConfigurationHandler.setDefaultConfigurable(globalConfigurable);

        final PaymentRetriesApi paymentRetriesApi = new PaymentRetriesApi(killbillAPI);

        final PaymentControlPluginApi paymentControlPluginApi = new PaymentRetriesPaymentControlPluginApi(paymentRetriesConfigurationHandler,
                                                                                                          paymentRetriesApi,
                                                                                                          killbillAPI,
                                                                                                          configProperties,
                                                                                                          logService,
                                                                                                          clock.getClock());
        registerPaymentControlPluginApi(context, paymentControlPluginApi);

        final PaymentRetriesServlet analyticsServlet = new PaymentRetriesServlet(paymentRetriesApi);
        registerServlet(context, analyticsServlet);

        registerEventHandler();
    }

    private void registerEventHandler() {
        final PluginConfigurationEventHandler eventHandler = new PluginConfigurationEventHandler(paymentRetriesConfigurationHandler);
        dispatcher.registerEventHandler(eventHandler);
    }

    private void registerServlet(final BundleContext context, final HttpServlet servlet) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Servlet.class, servlet, props);
    }

    private void registerPaymentControlPluginApi(final BundleContext context, final PaymentControlPluginApi api) {
        final Dictionary<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, PaymentControlPluginApi.class, api, props);
    }
}
