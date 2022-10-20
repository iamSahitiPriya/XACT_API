/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.client;

import com.xact.assessment.dtos.AccountResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.Map;



@Client("https://thoughtworks.okta.com")
public interface AccessTokenClient {

    @Post( value="/oauth2/aus1fjygi70z7ZtVB0h8/v1/token")
    @Header(name = "Content-type", value="application/x-www-form-urlencoded")
    AccountResponse getAccessToken(@Header String authorization, @Body Map<String, String> maps);


}
