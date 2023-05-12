/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.AccessControlRoleDto;
import com.xact.assessment.models.AccessControlList;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.repositories.AccessControlRepository;
import jakarta.inject.Singleton;

import java.util.List;
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

    public void saveRole(AccessControlRoleDto accessControlRole) {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.setEmail(accessControlRole.getEmail());
        accessControlList.setAccessControlRoles(accessControlRole.getAccessControlRoles());
        accessControlRepository.save(accessControlList);
    }

    public List<AccessControlList> getAllAccessControlRoles() {
        return accessControlRepository.findAll();
    }

    public void deleteUserRole(String email) {
        AccessControlList accessControlList = accessControlRepository.findById(email).orElse(new AccessControlList());
        if(accessControlList.getAccessControlRoles() != null) {
            accessControlRepository.delete(accessControlList);
        }
    }
}
