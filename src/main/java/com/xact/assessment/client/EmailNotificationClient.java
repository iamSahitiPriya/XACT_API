/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.client;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

@Client("${email.url}")
public interface EmailNotificationClient {

    @Post(value = "${email.endpoint}")
    HttpResponse sendNotification(@Header(AUTHORIZATION) String authorization, @Body String content);

}
