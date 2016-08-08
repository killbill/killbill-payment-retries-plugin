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

public enum ChasePaymentechAuthorizationDeclineCode implements AuthorizationDeclineCode {

    PREVIOUSLY_PROCESSED_TRANSACTION(109, "Previously Processed Transaction", true),
    INVALID_CC_NUMBER(201, "Invalid CC Number", true),
    BAD_AMOUNT(202, "Bad Amount", true),
    ZERO_AMOUNT(203, "Zero Amount", true),
    OTHER_ERROR(204, "Other Error", true),
    BAD_TOTAL_AUTHORIZATION_AMOUNT(205, "Bad Total Authorization Amount", true),
    INVALID_VALUE_IN_MESSAGE(225, "Invalid Value in Message", true),
    INVALID_DATA_TYPE(226, "Invalid Data Type", true),
    MISSING_COMPANION_DATA(227, "Missing Companion Data", true),
    INVALID_RECORD_SEQUENCE(228, "Invalid Record Sequence", true),
    PERCENTS_NOT_TOTAL_100(229, "Percents Not Total 100", true),
    PAYMENTS_NOT_TOTAL_ORDER(230, "Payments Not Total Order", true),
    INVALID_MERCHANT_NUMBER(231, "Invalid Merchant Number", true),
    BAD_ORDER_NUMBER(232, "Bad Order Number", true),
    DOES_NOT_MATCH_MOP(233, "Does not Match MOP", true),
    DUPLICATED_ORDER(234, "Duplicated Order #", true),
    FPO_LOCKED(235, "FPO Locked", true),
    AUTH_RECYCLE_HOST_DOWN(236, "Auth Recycle Host down", true),
    FPO_NOT_ALLOWED(237, "FPO Not Allowed", true),
    INVALID_CURRENCY(238, "Invalid Currency", true),
    INVALID_MOP_FOR_DIVISION(239, "Invalid MOP for Division", true),
    AUTH_AMOUNT_WRONG(240, "Auth Amount Wrong", true),
    ILLEGAL_ACTION(241, "Illegal Action", true),
    INVALID_PURCH__LEVEL_3(243, "Invalid Purch. Level 3", true),
    INVALID_SECURE_PAYMENT_DATA(245, "Invalid Secure Payment Data", true),
    MERCHANT_NOT_MC_SECURECODE_ENABLED(246, "Merchant not MC SecureCode Enabled", true),
    BLANKS_NOT_PASSED_IN_RESERVED_FIELD(248, "Blanks not Passed in Reserved Field", true),
    INVALID_MCC_SENT(249, "Invalid MCC Sent", true),
    INVALID_START_DATE(251, "Invalid Start Date", true),
    INVALID_ISSUE_NUMBER(252, "Invalid Issue Number", true),
    INVALID_TRANSACTION_TYPE(253, "Invalid Transaction Type", true),
    CUSTOMER_SERVICE_PHONE_NUMBER_REQUIRED(257, "Customer Service Phone Number required on Transaction Types 1 (MO/TO) and 2 (Recurring). MC Only", true),
    NOT_AUTHORIZED_TO_SEND_RECORD(258, "Not Authorized to send record", true),
    SOFT_AVS(260, "Soft AVS", true),
    ISSUER_UNAVAILABLE(301, "Issuer Unavailable", true),
    CREDIT_FLOOR(302, "Credit Floor", true),
    PROCESSOR_DECLINE(303, "Processor Decline", true),
    NOT_ON_FILE(304, "Not on File", true),
    FLOOR_LOW_FRAUD(332, "Floor Low Fraud", true),
    FLOOR_MEDIUM_FRAUD(333, "Floor Medium Fraud", true),
    FLOOR_HIGH_FRAUD(334, "Floor High fraud", true),
    FLOOR_UNAVAILABLE_FRAUD(335, "Floor Unavailable Fraud", true),
    CALL_REFER_TO_CARD_ISSUER(401, "Call/Refer to Card Issuer", true),
    DEFAULT_CALL(402, "Default Call", true),
    CALL_LOW_FRAUD(432, "Call Low Fraud", true),
    CALL_MEDIUM_FRAUD(433, "Call Medium Fraud", true),
    CALL_HIGH_FRAUD(434, "Call High Fraud", true),
    CALL_UNAVAILABLE_FRAUD(435, "Call Unavailable Fraud", true),
    DEFAULT_CALL_LOW_FRAUD(442, "Default Call Low Fraud", true),
    DEFAULT_CALL_MEDIUM_FRAUD(443, "Default Call Medium Fraud", true),
    DEFAULT_CALL_HIGH_FRAUD(444, "Default Call High Fraud", true),
    DEFAULT_CALL_UNAVAILABLE_FRAUD(445, "Default Call Unavailable Fraud", true),
    PICKUP(501, "Pickup", true),
    LOST_STOLEN(502, "Lost/Stolen", true),
    FRAUD(503, "Fraud", true),
    BAD_DEBT(504, "Bad Debt", true),
    ON_NEGATIVE_FILE(505, "On Negative File", true),
    INVALID_ISSUER(506, "Invalid Issuer", true),
    INVALID_RESPONSE_CODE(507, "Invalid Response Code", true),
    EXCESSIVE_PIN_TRY(508, "Excessive PIN Try", true),
    OVER_LIMIT(509, "Over Limit", true),
    OVER_FREQ_LIMIT(510, "Over Freq Limit", true),
    OVER_SAV_LIMIT(511, "Over Sav Limit", true),
    OVER_SAV_FREQ(512, "Over Sav Freq", true),
    OVER_CREDIT_FREQ(514, "Over Credit Freq", true),
    INVALID_FOR_CREDIT(515, "Invalid For Credit", true),
    INVALID_FOR_DEBIT(516, "Invalid For Debit", true),
    REV_EXCEED_WITHDRAWAL(517, "Rev Exceed Withdrawal", true),
    ONE_PURCHASING_LIMIT(518, "One Purchasing Limit", true),
    ON_NEGATIVE_FILE_2(519, "On Negative File", true),
    CHANGED_FIELD(520, "Changed Field", true),
    INSUFFICIENT_FUNDS(521, "Insufficient Funds", true),
    CARD_IS_EXPIRED(522, "Card is Expired", true),
    ENCRYPTED_DATA_BAD(523, "Encrypted Data Bad", true),
    ALTERED_DATA(524, "Altered Data", true),
    DO_NOT_HONOR(530, "Do Not Honor", true),
    CVV2_CVC2_FAILURE(531, "CVV2/CVC2 Failure", true),
    DO_NOT_HONOR_LOW_FRAUD(532, "Do Not Honor Low Fraud", true),
    DO_NOT_HONOR_MEDIUM_FRAUD(533, "Do Not Honor Medium Fraud", true),
    DO_NOT_HONOR_HIGH_FRAUD(534, "Do Not Honor High fraud", true),
    DO_NOT_HONOR_UNAVAILABLE_FRAUD(535, "Do Not Honor Unavailable Fraud", true),
    UNDER_18_YEARS_OLD(540, "Under 18 Years Old", true),
    POSSIBLE_COMPROMISE(541, "Possible Compromise", true),
    BILL_TO_NOT_EQUAL_TO_SHIP_TO(542, "Bill To Not Equal To Ship To", true),
    INVALID_PRE_APPROVAL_NUMBER(543, "Invalid Pre-approval Number", true),
    INVALID_EMAIL_ADDRESS(544, "Invalid Email Address", true),
    PA_ITA_NUMBER_INACTIVE(545, "PA ITA Number Inactive", true),
    BLOCKED_ACCOUNT(546, "Blocked Account", true),
    ADDRESS_VERIFICATION_FAILED(547, "Address Verification Failed", true),
    NOT_ON_CREDIT_BUREAU(548, "Not on Credit Bureau", true),
    PREVIOUSLY_DECLINED(549, "Previously Declined", true),
    CLOSED_ACCOUNT_NEW_ACCOUNT_CLOSED(550, "Closed Account, New Account Closed", true),
    DUPLICATE_TRANSACTION(551, "Duplicate Transaction", true),
    RE_AUTHORIZATION(560, "Re-Authorization", true),
    RE_AUTHORIZATION_NO_MATCH(561, "Re-Authorization – No Match", true),
    RE_AUTHORIZATION_TIMEFRAMES_EXCEEDED(563, "Re-Authorization – Timeframes Exceeded", true),
    STOP_DEPOSIT_ORDER(570, "Stop Deposit Order", true),
    REVOCATION_OF_AUTHORIZATION(571, "Revocation of Authorization", true),
    ACCOUNT_PREVIOUSLY_ACTIVATED(580, "Account Previously Activated", true),
    UNABLE_TO_VOID_TRANSACTION(581, "Unable to Void Transaction", true),
    BLOCK_ACTIVATION_FAILED_CARD_RANGE_NOT_SET_UP_FOR_MOD_10(582, "Block Activation Failed – Card Range Not Set Up for MOD 10", true),
    BLOCK_ACTIVATION_FAILED_E_MAIL_OR_FULFILLMENT_FLAGS_WERE_SET_TO_Y(583, "Block Activation Failed – E-mail or Fulfillment Flags were set to Y", true),
    DECLINED_ISSUANCE_DOES_NOT_MEET_MINIMUM_AMOUNT(584, "Declined – Issuance Does Not Meet Minimum Amount", true),
    DECLINED_NO_ORIGINAL_AUTH_FOUND(585, "Declined – No Original Auth Found", true),
    DECLINED_OUTSTANDING_AUTH_FUNDS_ON_HOLD(586, "Declined – Outstanding Auth, Funds On Hold", true),
    ACTIVATION_AMOUNT_INCORRECT(587, "Activation Amount Incorrect", true),
    BLOCK_ACTIVATION_FAILED_ACCOUNT_NOT_CORRECT_OR_BLOCK_SIZE_NOT_CORRECT(588, "Block Activation Failed – Account Not Correct Or Block Size Not Correct", true),
    MAG_STRIPE_CVD_VALUE_FAILED(589, "Mag Stripe CVD Value Failed", true),
    MAX_REDEMPTION_LIMIT_MET(590, "Max Redemption Limit Met", true),
    INVALID_CREDIT_CARD_NUMBER(591, "Invalid Credit Card Number", true),
    BAD_AMOUNT_2(592, "Bad Amount", true),
    OTHER_ERROR_2(594, "Other Error", true),
    NEW_CARD_ISSUED(595, "New Card Issued", true),
    ISSUER_HAS_FLAGGED_ACCOUNT_AS_SUSPECTED_FRAUD(596, "Issuer has Flagged Account as Suspected Fraud. (Discover Only)", true),
    INVALID_PREFIX(601, "Invalid Prefix", true),
    INVALID_INSTITUTION_CODE(602, "Invalid Institution Code", true),
    INVALID_INSTITUTION(603, "Invalid Institution", true),
    INVALID_CARDHOLDER(604, "Invalid Cardholder", true),
    INVALID_EXPIRATION_DATE(605, "Invalid Expiration Date", true),
    INVALID_TRANSACTION_TYPE_2(606, "Invalid Transaction Type", true),
    INVALID_AMOUNT(607, "Invalid Amount", true),
    BIN_BLOCK(610, "BIN Block", true),
    INVALID_TRANSIT_ROUTING_NUMBER(750, "Invalid Transit Routing Number", true),
    UNKNOWN_TRANSIT_ROUTING_NUMBER(751, "Unknown Transit Routing Number", true),
    MISSING_NAME(752, "Missing Name", true),
    INVALID_ACCOUNT_TYPE(753, "Invalid Account Type", true),
    ACCOUNT_CLOSED(754, "Account Closed", true),
    NO_ACCOUNT_UNABLE_TO_LOCATE(755, "No Account/Unable To Locate", true),
    ACCOUNT_HOLDER_DECEASED(756, "Account Holder Deceased", true),
    BENEFICIARY_DECEASED(757, "Beneficiary Deceased", true),
    ACCOUNT_FROZEN(758, "Account Frozen", true),
    CUSTOMER_OPT_OUT(759, "Customer Opt Out", true),
    ACH_NON_PARTICIPANT(760, "ACH Non-Participant", true),
    NO_PRE_NOTE(761, "No Pre-note", true),
    NO_ADDRESS(762, "No Address", true),
    INVALID_ACCOUNT_NUMBER(763, "Invalid Account Number", true),
    AUTHORIZATION_REVOKED_BY_CONSUMER(764, "Authorization Revoked by Consumer", true),
    CUSTOMER_ADVISES_NOT_AUTHORIZED(765, "Customer Advises Not Authorized", true),
    INVALID_CECP_ACTION_CODE(766, "Invalid CECP Action Code", true),
    INVALID_ACCOUNT_FORMAT(767, "Invalid Account Format", true),
    BAD_ACCOUNT_NUMBER_DATA(768, "Bad Account Number Data", true),
    ACCOUNT_NON_CONVERTIBLE(769, "Account Non-Convertible", true),
    NO_CAPTURE(801, "No Capture", true),
    POSITIVE_ID(802, "Positive ID", true),
    NO_CREDIT_FUNCTION(803, "No Credit Function", true),
    NO_DEBIT_FUNCTION(804, "No Debit Function", true),
    REV_EXCEED_WITHDRAWAL_2(805, "Rev Exceed Withdrawal", true),
    RESTRAINT(806, "Restraint", true),
    CHANGED_FIELD_2(807, "Changed Field", true),
    TERMINAL_NOT_OWNED(808, "Terminal Not Owned", true),
    INVALID_TIME(809, "Invalid Time", true),
    INVALID_DATE(810, "Invalid Date", true),
    INVALID_AMEX_CID(811, "Invalid Amex CID", true),
    INVALID_TERMINAL_NUMBER(812, "Invalid Terminal Number", true),
    INVALID_PIN(813, "Invalid PIN", true),
    NO_MANUAL_KEY(814, "No Manual Key", true),
    NOT_SIGNED_IN(815, "Not Signed In", true),
    EXCESSIVE_PIN_TRY_2(816, "Excessive PIN Try", true),
    NO_DDA(817, "No DDA", true),
    NO_SAV(818, "No SAV", true),
    EXCESS_DDA(819, "Excess DDA", true),
    EXCESS_DDA_FREQ(820, "Excess DDA FREQ", true),
    EXCESS_SAV(821, "Excess SAV", true),
    EXCESS_SAV_FREQ(822, "Excess SAV FREQ", true),
    EXCESS_CARD(823, "Excess Card", true),
    EXCESS_CARD_FREQ(824, "Excess Card Freq", true),
    NO_ACCOUNT(825, "No Account", true),
    RESERVED_FUTURE(826, "Reserved Future", true),
    RESERVED_CLOSING(827, "Reserved Closing", true),
    DORMANT(828, "Dormant", true),
    NSF(829, "NSF", true),
    FUTURE_RD_SIX(830, "Future RD Six", true),
    FUTURE_RD_SEVEN(831, "Future RD Seven", true),
    TRANSACTION_CODE_CONFLICT(832, "Transaction Code Conflict", true),
    INVALID_MERCHANT(833, "Invalid Merchant", true),
    METHOD_OF_PAYMENT_IS_INVALID_FOR_MERCHANT(834, "Method of Payment is Invalid for Merchant", true),
    IN_PROGRESS(901, "In Progress", true),
    PROCESS_UNAVAILABLE(902, "Process Unavailable", true),
    INVALID_EXPIRATION(903, "Invalid Expiration", true),
    INVALID_EFFECTIVE(904, "Invalid Effective", true),
    STAND_IN_RULES(905, "Stand In Rules", true);

    private final int code;
    private final String message;
    private final boolean retryable;

    ChasePaymentechAuthorizationDeclineCode(final int code, final String message, final boolean retryable) {
        this.code = code;
        this.message = message;
        this.retryable = retryable;
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
    public boolean isRetryable() {
        return retryable;
    }
}
