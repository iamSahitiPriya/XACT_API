/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.client;

import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.models.AccessTokenResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.Map;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;


@Client("${token.url}")
public interface AccessTokenClient {

    @Post( value="${token.endpoint}")
    @Header(name = "Content-type", value="application/x-www-form-urlencoded")
    AccessTokenResponse getAccessToken(@Header(AUTHORIZATION) String authorization, @Body Map<String, String> maps);
}
