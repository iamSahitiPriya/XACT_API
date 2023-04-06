/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.exceptions;


import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.http.hateoas.Resource;
import io.micronaut.http.server.exceptions.response.Error;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Produces
@Singleton
@Replaces(value = ErrorResponseProcessor.class)
public class GlobalErrorResponseProcessor implements ErrorResponseProcessor<JsonError> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorResponseProcessor.class);

    @Override
    public MutableHttpResponse<JsonError> processResponse(ErrorContext errorContext, MutableHttpResponse<?> response) {
        LOGGER.error("URI: {}", errorContext.getRequest().getUri());

        if (errorContext.getRequest().getMethod() == HttpMethod.HEAD) {
            return (MutableHttpResponse<JsonError>) response;
        }
        JsonError error;
        if (!errorContext.hasErrors()) {
            error = new JsonError(response.getStatus().getReason());
        } else if (errorContext.getErrors().size() == 1) {
            Error jsonError = errorContext.getErrors().get(0);
            error = new JsonError(jsonError.getMessage());
            jsonError.getPath().ifPresent(error::path);
        } else {
            error = new JsonError(response.getStatus().getReason());
            List<Resource> errors = new ArrayList<>();
            for (Error jsonError : errorContext.getErrors()) {
                errors.add(new JsonError(jsonError.getMessage()).path(jsonError.getPath().orElse(null)));
            }
            error.embedded("errors", errors);
        }
        error.link(Link.SELF, Link.of(errorContext.getRequest().getUri()));

        LOGGER.error("Exception log: {}", error);

        return HttpResponse.status(response.status());
    }
}
