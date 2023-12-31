/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class AssessmentMasterDataControllerTest {
    @Inject
    @Client("/")
    HttpClient client; //

    @Test
    void testGetMasterDataCategoryResponse() {
        var actualResult = client.toBlocking().exchange(HttpRequest.GET("/v1/assessments/1/categories/all")
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), actualResult.getStatus());
    }

    @Test
    void testGetSelectedCategoryResponse() {
        String categoryDto = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments/1/categories")
                .bearerAuth("anything"), String.class);

        assertEquals("{}", categoryDto);
    }

}
