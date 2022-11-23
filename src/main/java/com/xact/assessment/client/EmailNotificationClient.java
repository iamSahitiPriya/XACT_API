/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.client;

import com.xact.assessment.dtos.NotificationRequest;
import com.xact.assessment.dtos.NotificationResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

@Client("${email.url}")
public interface EmailNotificationClient {

    @Post(value = "${email.endpoint}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    NotificationResponse sendNotification(@Header(AUTHORIZATION) String authorization, @Body NotificationRequest content);

}
