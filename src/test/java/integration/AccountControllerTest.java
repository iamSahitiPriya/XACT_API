/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.models.Accounts;
import com.xact.assessment.repositories.AccountRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.io.IOException;

@MicronautTest
public class AccountControllerTest {


    @Client("/")
    @Inject
    HttpClient client;

    @Inject
    AccountRepository accountRepository;

    @Inject
    EntityManager entityManager;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    void getOrganisationNames() throws IOException {
        Accounts accounts = new Accounts("abc","Shell Singapore","Commodities, Utilities and Energy");
        Accounts accounts1 = new Accounts("def","Equinor","Finance");
        accountRepository.save(accounts);
        accountRepository.save(accounts1);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String organisationResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/accounts/shell")
                .bearerAuth("anything"), String.class);

        String expectedResponse = "[{" + "\"name\"" + ":" + "\"Shell Singapore\"" + "," +"\"industry\""+ ":"+"\"Commodities, Utilities and Energy\"" + "}]";

        Assertions.assertEquals(expectedResponse, organisationResponse);
    }
}
