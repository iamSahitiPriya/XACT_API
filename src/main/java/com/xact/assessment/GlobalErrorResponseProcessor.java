/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment;


import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
@Replaces(value = ErrorResponseProcessor.class)
public class GlobalErrorResponseProcessor implements ErrorResponseProcessor<HttpResponse> {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorResponseProcessor.class);

    @Override
    public MutableHttpResponse processResponse(ErrorContext errorContext, MutableHttpResponse baseResponse) {
        LOGGER.error("URI: {}", errorContext.getRequest().getUri());
        LOGGER.error("Exception: {}", errorContext.getRootCause());
        return baseResponse;
    }
}
