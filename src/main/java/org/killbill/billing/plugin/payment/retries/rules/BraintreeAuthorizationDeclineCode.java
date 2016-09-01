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

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BraintreeAuthorizationDeclineCode implements AuthorizationDeclineCode {

    DO_NOT_HONOR(2000, "Do Not Honor", true),
    INSUFFICIENT_FUNDS(2001, "Insufficient Funds", ErrorMessage.INSUFFICIENT_FUNDS, true),
    LIMIT_EXCEEDED(2002, "Limit Exceeded", true),
    CARDHOLDER_ACTIVITY_LIMIT_EXCEEDED(2003, "Cardholder's Activity Limit Exceeded", true),
    EXPIRED_CARD(2004, "Expired Card", ErrorMessage.EXPIRED_CARD, false),
    INVALID_CREDIT_CARD_NUMBER(2005, "Invalid Credit Card Number", ErrorMessage.CARD_NUMBER_MISMATCH, false),
    INVALID_EXPIRATION_DATE(2006, "Invalid Expiration Date", ErrorMessage.EXPIRATION_DATE_MISMATCH, false),
    NO_ACCOUNT(2007, "No Account", false),
    CARD_ACCOUNT_LENGTH_ERROR(2008, "Card Account Length Error", false),
    NO_SUCH_ISSUER(2009, "No Such Issuer", true),
    CARD_ISSUER_DECLINED_CVV(2010, "Card Issuer Declined CVV", ErrorMessage.CVV_MISMATCH, false),
    VOICE_AUTHORIZATION_REQUIRED(2011, "Voice Authorization Required", false),
    PROCESSOR_DECLINED_POSSIBLE_LOST_CARD(2012, "Processor Declined Possible Lost Card", ErrorMessage.LOST_OR_STOLEN, false),
    PROCESSOR_DECLINED_POSSIBLE_STOLEN_CARD(2013, "Processor Declined - Possible Stolen Card", ErrorMessage.LOST_OR_STOLEN, false),
    PROCESSOR_DECLINED_FRAUD_SUSPECTED(2014, "Processor Declined - Fraud Suspected", ErrorMessage.FRAUD, false),
    TRANSACTION_NOT_ALLOWED(2015, "Transaction Not Allowed", false),
    DUPLICATE_TRANSACTION(2016, "Duplicate Transaction", true),
    CARDHOLDER_STOPPED_BILLING(2017, "Cardholder Stopped Billing", false),
    CARDHOLDER_STOPPED_ALL_BILLING(2018, "Cardholder Stopped All Billing", false),
    INVALID_TRANSACTION(2019, "Invalid Transaction", false),
    VIOLATION(2020, "Violation", false),
    SECURITY_VIOLATION(2021, "Security Violation", true),
    DECLINED_UPDATED_CARDHOLDER_AVAILABLE(2022, "Declined - Updated Cardholder Available", false),
    PROCESSOR_DOES_NOT_SUPPORT_THIS_FEATURE(2023, "Processor Does Not Support This Feature", false),
    CARD_TYPE_NOT_ENABLED(2024, "Card Type Not Enabled", false),
    SET_UP_ERROR_MERCHANT(2025, "Set Up Error - Merchant", true),
    INVALID_MERCHANT_ID(2026, "Invalid Merchant ID", true),
    SET_UP_ERROR_AMOUNT(2027, "Set Up Error - Amount", false),
    SET_UP_ERROR_HIERARCHY(2028, "Set Up Error - Hierarchy", false),
    SET_UP_ERROR_CARD(2029, "Set Up Error - Card", false),
    SET_UP_ERROR_TERMINAL(2030, "Set Up Error - Terminal", false),
    ENCRYPTION_ERROR(2031, "Encryption Error", false),
    SURCHARGE_NOT_PERMITTED(2032, "Surcharge Not Permitted", false),
    INCONSISTENT_DATA(2033, "Inconsistent Data", true),
    NO_ACTION_TAKEN(2034, "No Action Taken", true),
    PARTIAL_APPROVAL_FOR_AMOUNT_IN_GROUP_III_VERSION(2035, "Partial Approval For Amount In Group III Version", true),
    AUTHORIZATION_COULD_NOT_BE_FOUND_TO_REVERSE(2036, "Authorization could not be found to reverse", false),
    ALREADY_REVERSED(2037, "Already Reversed", false),
    PROCESSOR_DECLINED(2038, "Processor Declined", true),
    INVALID_AUTHORIZATION_CODE(2039, "Invalid Authorization Code", false),
    INVALID_STORE(2040, "Invalid Store", true),
    DECLINED_CALL_FOR_APPROVAL(2041, "Declined - Call For Approval", false),
    INVALID_CLIENT_ID(2042, "Invalid Client ID", true),
    ERROR_DO_NOT_RETRY(2043, "Error - Do Not Retry, Call Issuer", false),
    DECLINED_CALL_ISSUER(2044, "Declined - Call Issuer", false),
    INVALID_MERCHANT_NUMBER(2045, "Invalid Merchant Number", false),
    DECLINED(2046, "Declined", true),
    CALL_ISSUER_PICK_UP_CARD(2047, "Call Issuer. Pick Up Card", false),
    INVALID_AMOUNT(2048, "Invalid Amount", true),
    INVALID_SKU_NUMBER(2049, "Invalid SKU Number", false),
    INVALID_CREDIT_PLAN(2050, "Invalid Credit Plan", true),
    CREDIT_CARD_NUMBER_DOES_NOT_MATCH_METHOD_OF_PAYMENT(2051, "Credit Card Number does not match method of payment", false),
    CARD_REPORTED_AS_LOST_OR_STOLEN(2053, "Card reported as lost or stolen", ErrorMessage.LOST_OR_STOLEN, false),
    REVERSAL_AMOUNT_DOES_NOT_MATCH_AUTHORIZATION_AMOUNT(2054, "Reversal amount does not match authorization amount", true),
    INVALID_TRANSACTION_DIVISION_NUMBER(2055, "Invalid Transaction Division Number", false),
    TRANSACTION_AMOUNT_EXCEEDS_THE_TRANSACTION_DIVISION_LIMIT(2056, "Transaction amount exceeds the transaction division limit", false),
    ISSUER_OR_CARDHOLDER_HAS_PUT_A_RESTRICTION_ON_THE_CARD(2057, "Issuer or Cardholder has put a restriction on the card", true),
    MERCHANT_NOT_MASTERCARD_SECURECODE_ENABLED(2058, "Merchant not MasterCard SecureCode enabled", false),
    ADDRESS_VERIFICATION_FAILED(2059, "Address Verification Failed", ErrorMessage.ADDRESS_MISMATCH, false),
    ADDRESS_VERIFICATION_AND_CARD_SECURITY_CODE_FAILED(2060, "Address Verification and Card Security Code Failed", ErrorMessage.ADDRESS_MISMATCH, false),
    INVALID_TRANSACTION_DATA(2061, "Invalid Transaction Data", false),
    INVALID_TAX_AMOUNT(2062, "Invalid Tax Amount", true),
    PAYPAL_BUSINESS_ACCOUNT_PREFERENCE_RESULTED_IN_THE_TRANSACTION_FAILING(2063, "PayPal Business Account preference resulted in the transaction failing", false),
    INVALID_CURRENCY_CODE(2064, "Invalid Currency Code", false),
    REFUND_TIME_LIMIT_EXCEEDED(2065, "Refund Time Limit Exceeded", false),
    PAYPAL_BUSINESS_ACCOUNT_RESTRICTED(2066, "PayPal Business Account Restricted", false),
    AUTHORIZATION_EXPIRED(2067, "Authorization Expired", false),
    PAYPAL_BUSINESS_ACCOUNT_LOCKED_OR_CLOSED(2068, "PayPal Business Account Locked or Closed", false),
    PAYPAL_BLOCKING_DUPLICATE_ORDER_IDS(2069, "PayPal Blocking Duplicate Order IDs", false),
    PAYPAL_BUYER_REVOKED_FUTURE_PAYMENT_AUTHORIZATION(2070, "PayPal Buyer Revoked Future Payment Authorization", false),
    PAYPAL_PAYEE_ACCOUNT_INVALID_OR_DOES_NOT_HAVE_A_CONFIRMED_EMAIL(2071, "PayPal Payee Account Invalid Or Does Not Have a Confirmed Email", false),
    PAYPAL_PAYEE_EMAIL_INCORRECTLY_FORMATTED(2072, "PayPal Payee Email Incorrectly Formatted", false),
    PAYPAL_VALIDATION_ERROR(2073, "PayPal Validation Error", false),
    FUNDING_INSTRUMENT_IN_THE_PAYPAL_ACCOUNT_WAS_DECLINED_BY_THE_PROCESSOR_OR_BANK(2074, "Funding Instrument In The PayPal Account Was Declined By The Processor Or Bank, Or It Can't Be Used For This Payment", false),
    PAYER_ACCOUNT_IS_LOCKED_OR_CLOSED(2075, "Payer Account Is Locked Or Closed", false),
    PAYER_CANNOT_PAY_FOR_THIS_TRANSACTION_WITH_PAYPAL(2076, "Payer Cannot Pay For This Transaction With PayPal", false),
    TRANSACTION_REFUSED_DUE_TO_PAYPAL_RISK_MODEL(2077, "Transaction Refused Due To PayPal Risk Model", false),
    PAYPAL_MERCHANT_ACCOUNT_CONFIGURATION_ERROR(2079, "PayPal Merchant Account Configuration Error", false),
    PAYPAL_PENDING_PAYMENTS_ARE_NOT_SUPPORTED(2081, "PayPal pending payments are not supported", false),
    PAYPAL_DOMESTIC_TRANSACTION_REQUIRED(2082, "PayPal Domestic Transaction Required", false),
    PAYPAL_PHONE_NUMBER_REQUIRED(2083, "PayPal Phone Number Required", false),
    PAYPAL_TAX_INFO_REQUIRED(2084, "PayPal Tax Info Required", false),
    PAYPAL_PAYEE_BLOCKED_TRANSACTION(2085, "PayPal Payee Blocked Transaction", false),
    PAYPAL_TRANSACTION_LIMIT_EXCEEDED(2086, "PayPal Transaction Limit Exceeded", false),
    PAYPAL_REFERENCE_TRANSACTIONS_NOT_ENABLED_FOR_YOUR_ACCOUNT(2087, "PayPal reference transactions not enabled for your account", false),
    CURRENCY_NOT_ENABLED_FOR_YOUR_PAYPAL_SELLER_ACCOUNT(2088, "Currency not enabled for your PayPal seller account", false),
    PAYPAL_PAYEE_EMAIL_PERMISSION_DENIED_FOR_THIS_REQUEST(2089, "PayPal payee email permission denied for this request", false),
    PAYPAL_ACCOUNT_NOT_CONFIGURED_TO_REFUND_MORE_THAN_SETTLED_AMOUNT(2090, "PayPal account not configured to refund more than settled amount", false),
    CURRENCY_OF_THIS_TRANSACTION_MUST_MATCH_CURRENCY_OF_YOUR_PAYPAL_ACCOUNT(2091, "Currency of this transaction must match currency of your PayPal account", false),
    PROCESSOR_DECLINED_92(2092, "Processor Declined", true),
    PROCESSOR_DECLINED_93(2093, "Processor Declined", true),
    PROCESSOR_DECLINED_94(2094, "Processor Declined", true),
    PROCESSOR_DECLINED_95(2095, "Processor Declined", true),
    PROCESSOR_DECLINED_96(2096, "Processor Declined", true),
    PROCESSOR_DECLINED_97(2097, "Processor Declined", true),
    PROCESSOR_DECLINED_98(2098, "Processor Declined", true),
    PROCESSOR_DECLINED_99(2099, "Processor Declined", true),
    PROCESSOR_NETWORK_UNAVAILABLE(3000, "Processor Network Unavailable - Try Again", true);

    private final int code;
    private final String message;
    private final ErrorMessage errorMessage;
    private final boolean retryable;

    BraintreeAuthorizationDeclineCode(final int code, final String message, final boolean retryable) {
        this(code, message, ErrorMessage.GENERAL_DECLINE, retryable);
    }

    BraintreeAuthorizationDeclineCode(final int code, final String message, final ErrorMessage errorMessage, final boolean retryable) {
        this.code = code;
        this.message = message;
        this.errorMessage = errorMessage;
        this.retryable = retryable;
    }

    @Override
    public String getProcessor() {
        return "Braintree";
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
