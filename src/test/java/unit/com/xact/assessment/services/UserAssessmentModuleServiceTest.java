/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.UserAssessmentModuleRepository;
import com.xact.assessment.services.UserAssessmentModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.Mockito.*;

public class UserAssessmentModuleServiceTest {
    private UserAssessmentModuleRepository userAssessmentModuleRepository;
    private UserAssessmentModuleService userAssessmentModuleService;

    @BeforeEach
    public void beforeEach(){
        userAssessmentModuleRepository = mock(UserAssessmentModuleRepository.class);
        userAssessmentModuleService = new UserAssessmentModuleService(userAssessmentModuleRepository);
    }

    @Test
    void shouldSaveUserAssessmentModule() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        when(userAssessmentModuleRepository.save(userAssessmentModule)).thenReturn(userAssessmentModule);
        userAssessmentModuleService.save(userAssessmentModule);

        verify(userAssessmentModuleRepository).save(userAssessmentModule);

    }
    @Test
    void shouldDeleteUserAssessmentModule() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        doNothing().when(userAssessmentModuleRepository).deleteByModule(assessment.getAssessmentId());
        userAssessmentModuleService.deleteByModule(assessment);

        verify(userAssessmentModuleRepository).deleteByModule(assessment.getAssessmentId());

    }
}
