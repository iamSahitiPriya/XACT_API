/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AccessControlRoleDto;
import com.xact.assessment.models.AccessControlList;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.repositories.AccessControlRepository;
import com.xact.assessment.services.AccessControlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccessControlServiceTest {
    private final AccessControlRepository accessControlRepository = Mockito.mock(AccessControlRepository.class);
    private final AccessControlService accessControlService = new AccessControlService(accessControlRepository);

    @Test
    void shouldSaveUserRole() {
        AccessControlRoleDto roleDto = new AccessControlRoleDto();
        roleDto.setEmail("abc@thoughtworks.com");
        roleDto.setAccessControlRoles(AccessControlRoles.PRIMARY_ADMIN);

        accessControlService.saveRole(roleDto);

        verify(accessControlRepository).save(any(AccessControlList.class));
    }

    @Test
    void shouldGetAllAccessControlRoles() {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.setEmail("abc@thoughtworks.com");
        accessControlList.setAccessControlRoles(AccessControlRoles.PRIMARY_ADMIN);

        when(accessControlRepository.findAll()).thenReturn(Collections.singletonList(accessControlList));

        List<AccessControlList> accessControlLists = accessControlService.getAllAccessControlRoles();

        Assertions.assertEquals(1, accessControlLists.size());
    }

    @Test
    void shouldDeleteRole() {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.setEmail("abc@thoughtworks.com");
        accessControlList.setAccessControlRoles(AccessControlRoles.PRIMARY_ADMIN);

        when(accessControlRepository.findById("abc@thoughtworks.com")).thenReturn(Optional.of(accessControlList));

        accessControlService.deleteUserRole("abc@thoughtworks.com");

        verify(accessControlRepository).delete(any(AccessControlList.class));

    }
}
