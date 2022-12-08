/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.exceptions;


import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
@Replaces(value = ErrorResponseProcessor.class)
public class GlobalErrorResponseProcessor implements ErrorResponseProcessor<JsonError> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorResponseProcessor.class);

    @Override
    public MutableHttpResponse<JsonError> processResponse(ErrorContext errorContext, MutableHttpResponse<?> baseResponse) {
        LOGGER.error("URI: {}", errorContext.getRequest().getUri());
        LOGGER.error("Exception: {}", errorContext.getRootCause());
        return HttpResponse.status(baseResponse.status());
    }
}
