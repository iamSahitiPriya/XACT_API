/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.annotations;

import com.xact.assessment.exceptions.UnauthorisedUserException;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AccessControlService;
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
    private final AccessControlService accessControlService;

    public AdminAuthInterceptor(UserAuthService userAuthService, AccessControlService accessControlService) {
        this.userAuthService = userAuthService;
        this.accessControlService = accessControlService;
    }

    @Override
    public Object intercept(MethodInvocationContext<Authentication, Object> context) {

        context.getParameterValueMap().forEach((name, value) -> {
            if (name.equals("authentication")) {
                Authentication authentication = (Authentication) value;
                User loggedInUser = userAuthService.getCurrentUser(authentication);
                Optional<AccessControlRoles> accessControlRoles = accessControlService.getAccessControlRolesByEmail(loggedInUser.getUserEmail());
                if (!(accessControlRoles.isPresent() && (accessControlRoles.get() == AccessControlRoles.PRIMARY_ADMIN || accessControlRoles.get() == AccessControlRoles.SECONDARY_ADMIN))) {
                    throw new UnauthorisedUserException("User not Authorised");
                }
            }
        });
        return context.proceed();

    }
}

