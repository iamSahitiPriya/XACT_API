/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.dtos.QuestionRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ModuleContributorRepository;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.ModuleService;
import com.xact.assessment.services.ParameterService;
import com.xact.assessment.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ModuleContributorServiceTest {
    private ModuleContributorRepository moduleContributorRepository;
    private ModuleContributorService moduleContributorService;
    private final QuestionService questionService = mock(QuestionService.class);
    private final ParameterService parameterService = mock(ParameterService.class);

    private ModuleService moduleService;

    @BeforeEach
    public void beforeEach() {
        moduleContributorRepository = mock(ModuleContributorRepository.class);
        moduleService = mock(ModuleService.class);
        moduleContributorService = new ModuleContributorService(questionService,parameterService, moduleContributorRepository,moduleService);

    }

    @Test
    void shouldGetModulesByRole() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("module");

        when(moduleContributorRepository.findByRole("smss@thoughtworks.com", ContributorRole.AUTHOR)).thenReturn(Collections.singletonList(assessmentModule));

        moduleContributorService.getModulesByRole("smss@thoughtworks.com", ContributorRole.AUTHOR);

        verify(moduleContributorRepository).findByRole("smss@thoughtworks.com", ContributorRole.AUTHOR);
    }

    @Test
    void shouldGetRoleByModuleAndUserEmail() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("module");
        String userEmail = "smss@thoughtworks.com";

        when(moduleContributorRepository.findRole(assessmentModule.getModuleId(), userEmail)).thenReturn(Optional.of(ContributorRole.AUTHOR));

        moduleContributorService.getRole(assessmentModule.getModuleId(), userEmail);

        verify(moduleContributorRepository).findRole(assessmentModule.getModuleId(), userEmail);
    }

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

        when(moduleContributorRepository.saveAll(any())).thenReturn(any());
        when(moduleService.getModule(moduleId)).thenReturn(assessmentModule);

        moduleContributorService.saveContributors(moduleId, Collections.singletonList(contributorDto));

        verify(moduleContributorRepository).saveAll(any());
    }

    @Test
    void shouldCreateQuestion() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("hello");
        questionRequest.setParameter(1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentParameter.setTopic(assessmentTopic);
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);
        assessmentModule.setModuleId(1);
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
        List<Question> questions = new ArrayList<>();
        Integer parameterId = 1;
        when(questionService.getAllQuestions()).thenReturn(questions);
        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));
        when(moduleContributorRepository.findRole(1,"abc@thoughtworks.com")).thenReturn(Optional.of(ContributorRole.AUTHOR));

        moduleContributorService.createAssessmentQuestion("abc@thoughtworks.com",questionRequest);
        verify(questionService).createQuestion(Optional.of(ContributorRole.AUTHOR),question);
    }
}
