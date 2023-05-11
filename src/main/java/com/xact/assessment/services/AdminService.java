/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import jakarta.inject.Singleton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class AdminService {
    private final ModuleContributorService moduleContributorService;

    private final AssessmentMasterDataService assessmentMasterDataService;
    private final UserAuthService userAuthService;

    private final AssessmentService assessmentService;
    private final AccessControlService accessControlService;


    public AdminService(ModuleContributorService moduleContributorService, AssessmentMasterDataService assessmentMasterDataService, UserAuthService userAuthService, AssessmentService assessmentService, AccessControlService accessControlService) {
        this.moduleContributorService = moduleContributorService;
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.userAuthService = userAuthService;
        this.assessmentService = assessmentService;
        this.accessControlService = accessControlService;
    }

    public void saveContributors(Integer moduleId, List<ContributorDto> contributors) {
        moduleContributorService.saveContributors(moduleId,contributors);
    }

    public AssessmentCategory createAssessmentCategory(AssessmentCategoryRequest assessmentCategory) {
        return assessmentMasterDataService.createAssessmentCategory(assessmentCategory);
    }

    public AssessmentModule createAssessmentModule(AssessmentModuleRequest assessmentModule) {
        return assessmentMasterDataService.createAssessmentModule(assessmentModule);
    }

    public AssessmentCategory updateCategory(AssessmentCategory assessmentCategory, AssessmentCategoryRequest assessmentCategoryRequest) {
        return assessmentMasterDataService.updateCategory(assessmentCategory,assessmentCategoryRequest);
    }

    public AssessmentModule updateModule(Integer moduleId, AssessmentModuleRequest assessmentModuleRequest) {
        return assessmentMasterDataService.updateModule(moduleId,assessmentModuleRequest);
    }

    public List<Assessment> getTotalAssessments(String startDate, String endDate) throws ParseException {
        return assessmentService.getTotalAssessments(startDate, endDate);
    }

    public AssessmentCategory getCategory(Integer categoryId) {
        return assessmentMasterDataService.getCategory(categoryId);
    }

    public void saveRole(AccessControlRoleDto accessControlRole) {
        accessControlService.saveRole(accessControlRole);
    }

    public List<AccessControlResponse> getAllAccessControlRoles() {
        List<AccessControlList> accessControlLists = accessControlService.getAllAccessControlRoles();
        List<AccessControlResponse> accessControlResponses = new ArrayList<>();
        for (AccessControlList user : accessControlLists) {
            UserInfo userInfo = userAuthService.getUserInfo(user.getEmail());
            AccessControlResponse accessControlResponse = new AccessControlResponse();
            if(userInfo != null){
                accessControlResponse.setUsername(userInfo.getFirstName()+' '+userInfo.getLastName());
            }else{
                accessControlResponse.setUsername(user.getEmail());
            }
            accessControlResponse.setEmail(user.getEmail());
            accessControlResponse.setAccessControlRoles(user.getAccessControlRoles());
            accessControlResponses.add(accessControlResponse);
        }
        return  accessControlResponses;
    }
}
