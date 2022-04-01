package com.xact.assessment.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/health")
public class ServiceStatusController {
    @Get
    public HttpResponse status() {
        return HttpResponse.ok();
    }
}
