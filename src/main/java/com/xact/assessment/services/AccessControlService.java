/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.repositories.AccessControlRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class AccessControlService {
    private final AccessControlRepository accessControlRepository;

    public AccessControlService(AccessControlRepository accessControlRepository) {
        this.accessControlRepository = accessControlRepository;
    }

    public Optional<AccessControlRoles> getAccessControlRolesByEmail(String email) {
        return accessControlRepository.getAccessControlRolesByEmail(email);
    }
}
