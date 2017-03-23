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

import org.killbill.billing.plugin.payment.retries.api.AuthorizationDeclineCode;
import org.killbill.billing.plugin.payment.retries.api.ErrorMessage;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AdyenAuthorizationDeclineCode implements AuthorizationDeclineCode {

    // See https://docs.adyen.com/support/payments-reporting
    REFER_TO_CARD_ISSUER(1, "Refer to card issuer", true),
    INVALID_MERCHANT(3, "Invalid merchant", true),
    CAPTURE_CARD(4, "Capture card", false),
    DO_NOT_HONOR(5, "Do not honor", true),
    ERROR(6, "Error", true),
    PICKUP_CARD(7, "Pickup card, special condition", false),
    INVALID_TRANSACTION(12, "Invalid transaction", true),
    INVALID_AMOUNT(13, "Invalid amount", true),
    INVALID_CARD_NUMBER(14, "Invalid card number", ErrorMessage.CARD_NUMBER_MISMATCH, true),
    INVALID_ISSUER(15, "Invalid issuer", true),
    FORMAT_ERROR(30, "Format error", true),
    LOST_CARD(41, "Lost card", ErrorMessage.LOST_OR_STOLEN, false),
    STOLEN_CARD(43, "Stolen card", ErrorMessage.LOST_OR_STOLEN, false),
    INSUFFICIENT_FUNDS(51, "Insufficient funds/over credit limit", ErrorMessage.INSUFFICIENT_FUNDS, true),
    EXPIRED_CARD(54, "Expired card", ErrorMessage.EXPIRED_CARD, true),
    INVALID_PIN(55, "Invalid PIN", true),
    TRANSACTION_NOT_PERMITTED(57, "Transaction not permitted to cardholder", true),
    SUSPECTED_FRAUD(59, "Suspected fraud", ErrorMessage.FRAUD, true),
    EXCEEDS_WITHDRAWAL_AMOUNT_LIMIT(61, "Exceeds withdrawal amount limit", true),
    RESTRICTED_CARD(62, "Restricted card", true),
    SECURITY_VIOLATION(63, "Security violation", true),
    EXCEEDS_WITHDRAWAL_COUNT_LIMIT(65, "Exceeds withdrawal count limit", true),
    CONTACT_CARD_ISSUER(70, "Contact Card Issuer", true),
    PIN_TRIES_EXCEEDED(75, "Allowable number of PIN tries exceeded", true),
    BLOCKED_FIRST_USED(78, "Blocked, first used", false),
    ISSUER_UNAVAILABLE(80, "Credit issuer unavailable", true),
    ISSUER_INOPERATIVE(91, "Issuer unavailable or switch inoperative", true),
    // ISSUER_UNAVAILABLE(91,"Authorization Platform or issuer system inoperative",true),
    UNABLE_TO_ROUTE_TRANSACTION(92, "Destination cannot be found for routing", true),
    // UNABLE_TO_ROUTE_TRANSACTION(92,"Unable to route transaction",true),
    VIOLATION_OF_LAW(93, "Transaction cannot be completed; violation of law", true),
    MALFUNCTION(96, "System malfunction", true);

    private final int code;
    private final String message;
    private final ErrorMessage errorMessage;
    private final boolean retryable;

    AdyenAuthorizationDeclineCode(final int code, final String message, final boolean retryable) {
        this(code, message, ErrorMessage.GENERAL_DECLINE, retryable);
    }

    AdyenAuthorizationDeclineCode(final int code, final String message, final ErrorMessage errorMessage, final boolean retryable) {
        this.code = code;
        this.message = message;
        this.errorMessage = errorMessage;
        this.retryable = retryable;
    }

    @Override
    public String getProcessor() {
        return "Adyen";
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean isRetryable() {
        return retryable;
    }
}
