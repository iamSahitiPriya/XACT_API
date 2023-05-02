/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.annotations;

import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.exceptions.UnauthorisedUserException;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.services.ModuleContributorService;
import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
@InterceptorBean(ContributorAuth.class)
public class ContributorAuthInterceptor implements MethodInterceptor<Authentication, Object> {
    private final ModuleContributorService moduleContributorService;

    public ContributorAuthInterceptor(ModuleContributorService moduleContributorService) {
        this.moduleContributorService = moduleContributorService;
    }

    @Override
    public Object intercept(MethodInvocationContext<Authentication, Object> context) {
        context.getParameterValueMap().forEach((name, value) -> {
            if (name.equals("authentication")) {
                Authentication authentication = (Authentication) value;
                Set<ModuleContributor> contributorRoles = moduleContributorService.getContributorsByEmail(authentication.getName());
                if (contributorRoles.stream().noneMatch(contributor -> (contributor.getContributorRole() == ContributorRole.AUTHOR ) || contributor.getContributorRole() == ContributorRole.REVIEWER)) {
                    throw new UnauthorisedUserException("User not Authorised");
                }
            }
        });
        return context.proceed();
    }
}
