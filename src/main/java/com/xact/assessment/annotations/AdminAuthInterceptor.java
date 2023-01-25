/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.annotations;

import com.xact.assessment.exceptions.UnauthorisedUserException;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
@InterceptorBean(AdminAuth.class)
public class AdminAuthInterceptor implements MethodInterceptor<Authentication, Object> {
    private final UserAuthService userAuthService;
    private final AssessmentService assessmentService;

    public AdminAuthInterceptor(UserAuthService userAuthService, AssessmentService assessmentService) {
        this.userAuthService = userAuthService;
        this.assessmentService = assessmentService;
    }

    @Override
    public Object intercept(MethodInvocationContext<Authentication, Object> context) {

        context.getParameterValueMap().forEach((name, value) -> {
            if (name.equals("authentication")) {
                Authentication authentication = (Authentication) value;
                User loggedInUser = userAuthService.getCurrentUser(authentication);
                Optional<AccessControlRoles> accessControlRoles = assessmentService.getUserRole(loggedInUser.getUserEmail());
                if (!(accessControlRoles.isPresent() && accessControlRoles.get() == AccessControlRoles.Admin)) {
                    throw new UnauthorisedUserException("User not Authorised");
                }
            }
        });
        return context.proceed();

    }
}

