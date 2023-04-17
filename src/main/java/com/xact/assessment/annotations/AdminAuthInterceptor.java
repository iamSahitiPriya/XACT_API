/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.annotations;

import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.exceptions.UnauthorisedUserException;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AccessControlService;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.Set;

@Singleton
@InterceptorBean(AdminAuth.class)
public class AdminAuthInterceptor implements MethodInterceptor<Authentication, Object> {
    private final UserAuthService userAuthService;
    private final AssessmentService assessmentService;
    private final AccessControlService accessControlService;

    public AdminAuthInterceptor(UserAuthService userAuthService, AssessmentService assessmentService, AccessControlService accessControlService) {
        this.userAuthService = userAuthService;
        this.assessmentService = assessmentService;
        this.accessControlService = accessControlService;
    }

    @Override
    public Object intercept(MethodInvocationContext<Authentication, Object> context) {

        context.getParameterValueMap().forEach((name, value) -> {
            if (name.equals("authentication")) {
                Authentication authentication = (Authentication) value;
                User loggedInUser = userAuthService.getCurrentUser(authentication);
                Optional<AccessControlRoles> accessControlRoles = accessControlService.getAccessControlRolesByEmail(loggedInUser.getUserEmail());
                if (!(accessControlRoles.isPresent() && accessControlRoles.get() == AccessControlRoles.Admin)) {
                    throw new UnauthorisedUserException("User not Authorised");
                }
            }
        });
        return context.proceed();

    }
}

