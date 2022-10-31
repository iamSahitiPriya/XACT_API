package com.xact.assessment.services;

import com.xact.assessment.dtos.OrganisationResponse;
import io.micronaut.cache.annotation.CacheAnnotation;
import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;
import org.hibernate.annotations.Cache;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
@CacheConfig(value = "organisation")
public class OrganisationService {

        Map<String, List<OrganisationResponse>> organisation = new HashMap<String, List<OrganisationResponse>>() {{
            put("user", List.of(new OrganisationResponse()));
            put("delectus aut autem", Collections.singletonList(new OrganisationResponse(1,1,"Id number one",false)));
        }};
    @Cacheable
    public List<OrganisationResponse> getOrganisationName(OrganisationResponse orgName){
        try {
            TimeUnit.SECONDS.sleep(3);
            System.out.println("INSIDE CATCH");
            return organisation.get(orgName.getTitle());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
