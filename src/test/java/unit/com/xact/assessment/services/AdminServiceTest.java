/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AccessControlResponse;
import com.xact.assessment.dtos.AccessControlRoleDto;
import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class AdminServiceTest {

    ModuleContributorService moduleContributorService = Mockito.mock(ModuleContributorService.class);

    AssessmentMasterDataService assessmentMasterDataService=Mockito.mock(AssessmentMasterDataService.class);

    AssessmentService assessmentService=Mockito.mock(AssessmentService.class);
    UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
    AccessControlService accessControlService = Mockito.mock(AccessControlService.class);

    private final AdminService adminService = new AdminService(moduleContributorService, assessmentMasterDataService, userAuthService, assessmentService, accessControlService);

    @Test
    void shouldSaveModuleContributor() {
        Integer moduleId = 1;
        ContributorDto contributorDto = new ContributorDto();
        contributorDto.setUserEmail("abc@thoughtworks.com");
        contributorDto.setRole(ContributorRole.AUTHOR);
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(moduleId);
        ContributorId contributorId = new ContributorId();
        contributorId.setModule(assessmentModule);
        contributorId.setUserEmail(contributorDto.getUserEmail());

        doNothing().when(moduleContributorService).saveContributors(moduleId, Collections.singletonList(contributorDto));
        adminService.saveContributors(moduleId, Collections.singletonList(contributorDto));

        verify(moduleContributorService).saveContributors(moduleId, Collections.singletonList(contributorDto));

    }

    @Test
    void shouldGetAccessControlRoles() {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.setEmail("abc@thoughtworks.com");
        accessControlList.setAccessControlRoles(AccessControlRoles.PRIMARY_ADMIN);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("abc@thoughtworks.com");
        userInfo.setFirstName("ABC");
        userInfo.setLastName("DEF");
        userInfo.setLocale("US");

        when(accessControlService.getAllAccessControlRoles()).thenReturn(Collections.singletonList(accessControlList));
        when(userAuthService.getUserInfo("abc@thoughtworks.com")).thenReturn(userInfo);

        List<AccessControlResponse> response = adminService.getAllAccessControlRoles();

        verify(accessControlService).getAllAccessControlRoles();
        verify(userAuthService).getUserInfo("abc@thoughtworks.com");
        Assertions.assertEquals(1, response.size());

    }

    @Test
    void shouldSaveUserRole() {
        AccessControlRoleDto roleDto = new AccessControlRoleDto();
        roleDto.setEmail("abc@thoughtworks.com");
        roleDto.setAccessControlRoles(AccessControlRoles.PRIMARY_ADMIN);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("def@thoughtworks.com");
        userInfo.setFirstName("ABC");
        userInfo.setLastName("DEF");
        userInfo.setLocale("US");
        User loggedInUser = new User();
        loggedInUser.setUserInfo(userInfo);

        doNothing().when(accessControlService).saveRole(roleDto);
        adminService.saveRole(roleDto);

        verify(accessControlService).saveRole(roleDto);
    }

    @Test
    void shouldDeleteUserRole() {
        AccessControlRoleDto roleDto = new AccessControlRoleDto();
        roleDto.setEmail("abc@thoughtworks.com");
        roleDto.setAccessControlRoles(AccessControlRoles.PRIMARY_ADMIN);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("def@thoughtworks.com");
        userInfo.setFirstName("ABC");
        userInfo.setLastName("DEF");
        userInfo.setLocale("US");
        User loggedInUser = new User();
        loggedInUser.setUserInfo(userInfo);

        doNothing().when(accessControlService).deleteUserRole(roleDto.getEmail());
        adminService.deleteUserRole(roleDto.getEmail(), loggedInUser);

        verify(accessControlService).deleteUserRole(roleDto.getEmail());
    }
    @Test
    void shouldThrowExceptionWhenUserTriedToDeleteThemself() throws RuntimeException {
        AccessControlRoleDto roleDto = new AccessControlRoleDto();
        roleDto.setEmail("abc@thoughtworks.com");
        roleDto.setAccessControlRoles(AccessControlRoles.PRIMARY_ADMIN);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("abc@thoughtworks.com");
        userInfo.setFirstName("ABC");
        userInfo.setLastName("DEF");
        userInfo.setLocale("US");
        User loggedInUser = new User();
        loggedInUser.setUserInfo(userInfo);

        Assertions.assertThrows(RuntimeException.class, ()->adminService.deleteUserRole(roleDto.getEmail(), loggedInUser));
    }
}
