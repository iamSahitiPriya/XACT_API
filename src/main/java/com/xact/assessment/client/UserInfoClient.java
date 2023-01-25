/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.client;

import com.xact.assessment.dtos.UserInfoDto;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;


@Client("${token.url}")
public interface UserInfoClient {

    @Post( value="${identityProvider.iss}${identityProvider.userInfo}")
    UserInfoDto getUserInfo(@Header(AUTHORIZATION) String authorization);
}
