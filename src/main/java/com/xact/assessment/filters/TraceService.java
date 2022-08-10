/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.filters;

import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

import static com.xact.assessment.constants.AppConstants.CORRELATION_ID;

@Singleton
public class TraceService {

    private static final Logger LOG = LoggerFactory.getLogger(TraceService.class);

    Publisher<Boolean> trace(HttpRequest<?> request, UUID requestId) {
        return Mono.fromCallable(() -> { //
            MDC.clear();
            MDC.put(CORRELATION_ID, String.valueOf(requestId));
            LOG.debug("Tracing request: {} : {}", request.getUri(), requestId);
            return true;
        }).subscribeOn(Schedulers.boundedElastic()) //
                .flux();
    }
}
