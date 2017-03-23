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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.killbill.billing.plugin.payment.retries.api.AuthorizationDeclineCode;

public class PaymentRetriesStats {

    private final Map<String, Map<Integer, AtomicLong>> nbOfAbortedRetriesPerProcessorPerCode = new HashMap<String, Map<Integer, AtomicLong>>();
    private final Map<String, Map<Integer, AtomicLong>> nbOfFailedRetriesPerProcessorPerCode = new HashMap<String, Map<Integer, AtomicLong>>();
    private final Map<String, Map<Integer, AtomicLong>> nbOfSuccessfulRetriesPerProcessorPerCode = new HashMap<String, Map<Integer, AtomicLong>>();

    public long getNbOfRetries(final AuthorizationDeclineCode authorizationDeclineCode) {
        return getNbOfRetries(nbOfFailedRetriesPerProcessorPerCode, authorizationDeclineCode) + getNbOfRetries(nbOfSuccessfulRetriesPerProcessorPerCode, authorizationDeclineCode);
    }

    public long markRetriedPaymentAsSuccessful(final AuthorizationDeclineCode authorizationDeclineCode) {
        return incrementAndGet(nbOfSuccessfulRetriesPerProcessorPerCode, authorizationDeclineCode);
    }

    // Note: that's the previous payment decline code
    public long markRetriedPaymentAsFailed(final AuthorizationDeclineCode authorizationDeclineCode) {
        return incrementAndGet(nbOfFailedRetriesPerProcessorPerCode, authorizationDeclineCode);
    }

    private long incrementAndGet(final Map<String, Map<Integer, AtomicLong>> statsMaps, final AuthorizationDeclineCode authorizationDeclineCode) {
        prime(statsMaps, authorizationDeclineCode);

        final Map<Integer, AtomicLong> processorStatsMap = statsMaps.get(authorizationDeclineCode.getProcessor());
        if (processorStatsMap.get(authorizationDeclineCode.getCode()) == null) {
            synchronized (processorStatsMap) {
                if (processorStatsMap.get(authorizationDeclineCode.getCode()) == null) {
                    processorStatsMap.put(authorizationDeclineCode.getCode(), new AtomicLong(0L));
                }
            }
        }
        return processorStatsMap.get(authorizationDeclineCode.getCode()).incrementAndGet();
    }

    private void prime(final Map<String, Map<Integer, AtomicLong>> map, final AuthorizationDeclineCode authorizationDeclineCode) {
        if (map.get(authorizationDeclineCode.getProcessor()) == null) {
            synchronized (map) {
                if (map.get(authorizationDeclineCode.getProcessor()) == null) {
                    map.put(authorizationDeclineCode.getProcessor(), new HashMap<Integer, AtomicLong>());
                }
            }
        }
    }

    private long getNbOfRetries(final Map<String, Map<Integer, AtomicLong>> statsMaps, final AuthorizationDeclineCode authorizationDeclineCode) {
        final Map<Integer, AtomicLong> processorStatsMap = statsMaps.get(authorizationDeclineCode.getProcessor());
        if (processorStatsMap != null) {
            final AtomicLong nbPerCode = processorStatsMap.get(authorizationDeclineCode.getCode());
            if (nbPerCode != null) {
                return nbPerCode.get();
            }
        }
        return 0;
    }
}
