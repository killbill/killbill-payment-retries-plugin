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

package org.killbill.billing.plugin.payment.retries.api;

import java.util.Arrays;
import java.util.function.Predicate;

import com.google.common.base.Strings;

public enum ErrorMessage implements Predicate<String> {

    ADDRESS_MISMATCH(false, "address"),
    CARD_NUMBER_MISMATCH(false, "invalid card number"),
    CVV_MISMATCH(false, "cvc"),
    EXPIRATION_DATE_MISMATCH(false, "expiration date"),
    EXPIRED_CARD(false, "expired"),
    FRAUD(false, "fraud"),
    GENERAL_DECLINE(true),
    INSUFFICIENT_FUNDS(true, "not enough balance", "withdrawal amount exceeded"),
    LOST_OR_STOLEN(false);

    private final String[] keywords;
    private final boolean retryable;

    ErrorMessage(final boolean retryable, final String... keywords) {
        this.keywords = keywords;
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return this.retryable;
    }

    @Override
    public boolean test(final String message) {
        if(Strings.isNullOrEmpty(message)) {
            return false;
        }
        return Arrays.stream(keywords).parallel().anyMatch(message.toLowerCase()::contains);
    }

    @Override
    public String toString() {
        return this.name();
    }
}
