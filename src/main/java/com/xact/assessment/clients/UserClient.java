package com.xact.assessment.clients;

import unit.com.xact.assessment.models.User;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;

import static io.micronaut.http.HttpHeaders.*;

@Client("${api.serverUrl}")
@Header(name = USER_AGENT, value = "Micronaut HTTP Client")
@Header(name = ACCEPT, value = "application/vnd.github.v3+json, application/json")
@Header(name = AUTHORIZATION, value = "SSWS ${api.key}")
public interface UserClient {

    @Get("/api/v1/users/{emailId}")
    User getActiveUser(String emailId);
}
