/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.exceptions;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
@Requires(classes = {InvalidContributorException.class, ExceptionHandler.class})
public class InvalidContributorExceptionHandler implements ExceptionHandler<InvalidContributorException, HttpResponse<Void>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidContributorExceptionHandler.class);

    @Override
    public HttpResponse<Void> handle(HttpRequest request, InvalidContributorException exception) {
        LOGGER.info("Invalid Request");
        return HttpResponse.badRequest();
    }
}
