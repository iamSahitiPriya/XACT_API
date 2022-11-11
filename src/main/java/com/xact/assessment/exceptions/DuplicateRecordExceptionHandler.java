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
@Requires(classes = {DuplicateRecordException.class, ExceptionHandler.class})
public class DuplicateRecordExceptionHandler implements ExceptionHandler<DuplicateRecordException, HttpResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DuplicateRecordExceptionHandler.class);

    @Override
    public HttpResponse handle(HttpRequest request, DuplicateRecordException exception) {
        LOGGER.info("Duplicate records are not allowed");
        return HttpResponse.serverError();
    }
}
