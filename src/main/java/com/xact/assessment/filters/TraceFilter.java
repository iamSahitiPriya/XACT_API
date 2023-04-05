/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.filters;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static com.xact.assessment.constants.AppConstants.CORRELATION_ID;

@Filter("/v1/**") //
public class TraceFilter implements HttpServerFilter {

    private final TraceService traceService;

    public TraceFilter(TraceService traceService) { //
        this.traceService = traceService;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request,
                                                      ServerFilterChain chain) {

        UUID requestId = UUID.randomUUID();

        return Flux.from(traceService
                .trace(request, requestId)) //
                .switchMap(
                        aBoolean -> chain.proceed(request)) //
                .doOnNext(res ->
                        res.getHeaders().add(CORRELATION_ID, String.valueOf(requestId)) //
                );
    }

}
