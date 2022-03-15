package com.xact.assessment.controller;

import com.xact.assessment.controllers.AssessmentController;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@MicronautTest
public class AssessmentControllerTest {

    @Inject
    private AssessmentController assessmentController;
    private Authentication authentication = Mockito.mock(Authentication.class);

    @Test
    public void testHandler() {
        Map<String, Object> authAttributes = new HashMap<>();
        authAttributes.put("name","dummy-user");
        authAttributes.put("email","dummy-user@tw.com");
        when(authentication.getAttributes()).thenReturn(authAttributes);

        HttpResponse assessmentResponse = assessmentController.get("assessment-id-123",authentication);

        assertNotNull(assessmentResponse);
    }
}
