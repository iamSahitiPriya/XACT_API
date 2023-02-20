package com.xact.assessment.exceptions;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
@Requires(classes = {InvalidHierarchyException.class, ExceptionHandler.class})
public class InvalidHierarchyExceptionHandler implements ExceptionHandler<InvalidHierarchyException, MutableHttpResponse<String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidHierarchyExceptionHandler.class);

    @Override
    public MutableHttpResponse<String> handle(HttpRequest request, InvalidHierarchyException exception) {
        LOGGER.info("Invalid hierarchy movement");
        return HttpResponse.serverError(exception.getMessage());
    }
}
