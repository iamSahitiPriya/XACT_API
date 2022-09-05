package com.xact.assessment.exceptions;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {UnauthorisedUserException.class, ExceptionHandler.class})
public class GlobalExceptionHandler implements ExceptionHandler<UnauthorisedUserException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, UnauthorisedUserException exception) {
        return HttpResponse.unauthorized();
    }
}
