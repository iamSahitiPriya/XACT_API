/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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
@Requires(classes = {UnauthorisedUserException.class, ExceptionHandler.class})
public class GlobalExceptionHandler implements ExceptionHandler<UnauthorisedUserException, HttpResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @Override
    public HttpResponse handle(HttpRequest request, UnauthorisedUserException exception) {
        LOGGER.info("Unauthorised user");
        return HttpResponse.unauthorized();
    }
}
