/*
 * Copyright 2014-2017 Groupon, Inc
 * Copyright 2014-2017 The Billing Project, LLC
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
public class DefaultAuthorizationDeclineCode implements AuthorizationDeclineCode {

    protected final String processor;
    protected final int code;
    protected final String message;
    protected final ErrorMessage errorMessage;
    protected final boolean retryable;

    DefaultAuthorizationDeclineCode(final String processor, final int code, final String message, final ErrorMessage errorMessage, final boolean retryable) {
        this.processor = processor;
        this.code = code;
        this.message = message;
        this.errorMessage = errorMessage;
        this.retryable = retryable;
    }

    @Override
    public String getProcessor() {
        return processor;
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
