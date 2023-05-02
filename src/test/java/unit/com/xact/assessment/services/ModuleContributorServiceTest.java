/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.exceptions.DuplicateRecordException;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ModuleContributorRepository;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ModuleContributorServiceTest {
    private ModuleContributorRepository moduleContributorRepository;
    private ModuleContributorService moduleContributorService;
    private final QuestionService questionService = mock(QuestionService.class);
    private final ParameterService parameterService = mock(ParameterService.class);
    private final TopicService topicService = mock(TopicService.class);

    private ModuleService moduleService;

    @BeforeEach
    public void beforeEach() {
        moduleContributorRepository = mock(ModuleContributorRepository.class);
        moduleService = mock(ModuleService.class);
        moduleContributorService = new ModuleContributorService(questionService, parameterService, topicService, moduleContributorRepository, moduleService);

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
        when(moduleContributorRepository.findRole(1, "abc@thoughtworks.com")).thenReturn(Optional.of(ContributorRole.AUTHOR));

        moduleContributorService.createAssessmentQuestion("abc@thoughtworks.com", questionRequest);
        verify(questionService).createQuestion(Optional.of(ContributorRole.AUTHOR), question);
    }

    @Test
    void shouldCreateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("topic");
        topicRequest.setModule(1);
        topicRequest.setComments("");
        topicRequest.setActive(false);

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentTopic assessmentTopic = new AssessmentTopic(topicRequest.getTopicName(), assessmentModule, topicRequest.isActive(), topicRequest.getComments());
        AssessmentTopic assessmentTopic1 = new AssessmentTopic(2, "new topic", assessmentModule, true, "");
        assessmentModule.setTopics(Collections.singleton(assessmentTopic1));
        when(moduleService.getModule(1)).thenReturn(assessmentModule);
        moduleContributorService.createAssessmentTopics(topicRequest);
        verify(topicService).createTopic(assessmentTopic);

    }

    @Test
    void shouldCreateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterId(1);
        parameterRequest.setParameterName("parameter");
        parameterRequest.setTopic(1);
        parameterRequest.setActive(false);
        parameterRequest.setComments("");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(1, "new parameter", assessmentTopic, null, null, true, new Date(), new Date(), "", 1);
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter1));
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterName(parameterRequest.getParameterName()).topic(assessmentTopic).isActive(parameterRequest.isActive()).comments(parameterRequest.getComments()).build();
        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        moduleContributorService.createAssessmentParameter(parameterRequest);
        verify(parameterService).createParameter(assessmentParameter);
    }


    @Test
    void shouldCreateTopicReferences() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference");
        topicReferencesRequest.setRating(RatingLevel.valueOf("TWO"));
        topicReferencesRequest.setReferenceId(1);

        AssessmentTopic topic = new AssessmentTopic();
        when(topicService.getTopic(1)).thenReturn(Optional.of(topic));
        moduleContributorService.createAssessmentTopicReference(topicReferencesRequest);
        AssessmentTopicReference assessmentTopicReference1 = new AssessmentTopicReference(topic, topicReferencesRequest.getRating(), topicReferencesRequest.getReference());

        doNothing().when(topicService).saveTopicReference(assessmentTopicReference1);
        verify(topicService).saveTopicReference(assessmentTopicReference1);

    }

    @Test
    void shouldCreateParameterReference() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(RatingLevel.valueOf("TWO"));

        AssessmentParameter parameter = new AssessmentParameter();
        when(parameterService.getParameter(1)).thenReturn(Optional.of(parameter));
        moduleContributorService.createAssessmentParameterReference(parameterReferencesRequest);
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(parameter, parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());

        doNothing().when(parameterService).saveParameterReference(assessmentParameterReference);
        verify(parameterService).saveParameterReference(assessmentParameterReference);
    }
    @Test
    void shouldUpdateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("topic");
        topicRequest.setModule(1);
        topicRequest.setComments("");
        topicRequest.setActive(false);

        AssessmentModule assessmentModule = new AssessmentModule("module", null, true, "");
        AssessmentTopic assessmentTopic = new AssessmentTopic("new topic", assessmentModule, topicRequest.isActive(), topicRequest.getComments());
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));

        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        when(moduleService.getModule(topicRequest.getModule())).thenReturn(assessmentModule);
        moduleContributorService.updateTopic(1, topicRequest);
        verify(topicService).updateTopic(assessmentTopic);
    }

    @Test
    void shouldUpdateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterId(1);
        parameterRequest.setParameterName("parameter");
        parameterRequest.setTopic(1);
        parameterRequest.setActive(false);
        parameterRequest.setComments("");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterName(parameterRequest.getParameterName()).topic(assessmentTopic).isActive(parameterRequest.isActive()).comments(parameterRequest.getComments()).build();
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(2, "new parameter", assessmentTopic, null, null, true, new Date(), new Date(), "", 1);
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter1));

        AssessmentParameterRequest assessmentParameterRequest = new AssessmentParameterRequest();
        assessmentParameterRequest.setTopic(1);
        assessmentParameterRequest.setParameterName("this is an updated parameter");
        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));
        moduleContributorService.updateParameter(1, assessmentParameterRequest);
        verify(parameterService).updateParameter(assessmentParameter);
    }

    @Test
    void shouldUpdateQuestions() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("hello");
        questionRequest.setParameter(1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);

        QuestionRequest questionRequest1 = new QuestionRequest();
        questionRequest1.setParameter(1);
        questionRequest1.setQuestionText("This is an updated question");
        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));
        when(questionService.getQuestion(1)).thenReturn(Optional.of(question));
        moduleContributorService.updateQuestion(1, questionRequest1);
        verify(questionService).updateQuestion(question);

    }

    @Test
    void shouldUpdateTopicReferences() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference");
        topicReferencesRequest.setRating(RatingLevel.valueOf("TWO"));
        topicReferencesRequest.setReferenceId(1);

        AssessmentTopic topic = new AssessmentTopic();
        when(topicService.getTopic(1)).thenReturn(Optional.of(topic));

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(topic, RatingLevel.TWO, "new reference");
        topic.setReferences(Collections.singleton(assessmentTopicReference));


        when(topicService.getAssessmentTopicReference(1)).thenReturn(assessmentTopicReference);
        moduleContributorService.updateTopicReference(1, topicReferencesRequest);
        verify(topicService).updateTopicReference(assessmentTopicReference);
    }

    @Test
    void shouldUpdateParameterReference() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(RatingLevel.valueOf("TWO"));

        AssessmentParameter parameter = new AssessmentParameter();
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(parameter, RatingLevel.TWO, "new reference");
        parameter.setReferences(Collections.singleton(assessmentParameterReference));
        when(parameterService.getParameter(1)).thenReturn(Optional.of(parameter));

        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setParameter(1);
        referencesRequest.setReference("UPDATE REFERENCE");
        when(parameterService.getAssessmentParameterReference(1)).thenReturn(assessmentParameterReference);
        moduleContributorService.updateParameterReference(1, referencesRequest);
        verify(parameterService).updateParameterReference(assessmentParameterReference);
    }
    @Test
    void deleteTopicReference() {
        doNothing().when(topicService).deleteTopicReference(1);

        moduleContributorService.deleteTopicReference(1);

        verify(topicService).deleteTopicReference(1);
    }

    @Test
    void deleteParameterReference() {
        doNothing().when(parameterService).deleteParameterReference(1);

        moduleContributorService.deleteParameterReference(1);

        verify(parameterService).deleteParameterReference(1);
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheTopicIsAlreadyPresent() {
        AssessmentTopicRequest assessmentTopicRequest = new AssessmentTopicRequest();
        assessmentTopicRequest.setTopicName("topic");
        assessmentTopicRequest.setModule(1);
        assessmentTopicRequest.setActive(true);
        assessmentTopicRequest.setComments("comments");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        AssessmentTopic assessmentTopic = new AssessmentTopic(1, "topic", assessmentModule, true, "");
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        when(moduleService.getModule(assessmentModule.getModuleId())).thenReturn(assessmentModule);

        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.createAssessmentTopics(assessmentTopicRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheParameterIsAlreadyPresent() {
        AssessmentParameterRequest assessmentParameterRequest = new AssessmentParameterRequest();
        assessmentParameterRequest.setParameterName("parameter");
        assessmentParameterRequest.setTopic(1);
        assessmentParameterRequest.setActive(true);
        assessmentParameterRequest.setComments("comments");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        AssessmentParameter assessmentParameter = new AssessmentParameter(1, "parameter", assessmentTopic, null, null, true, new Date(), new Date(), "", 1);
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));
        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));

        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.createAssessmentParameter(assessmentParameterRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheParameterReferenceIsAlreadyPresent() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(RatingLevel.ONE);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setReferences(Collections.singleton(new AssessmentParameterReference(assessmentParameter, RatingLevel.ONE, "reference")));
        assessmentParameter.setParameterId(1);
        when(parameterService.getParameter(assessmentParameter.getParameterId())).thenReturn(Optional.of(assessmentParameter));

        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.createAssessmentParameterReference(parameterReferencesRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheTopicReferenceIsAlreadyPresent() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference");
        topicReferencesRequest.setRating(RatingLevel.ONE);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setReferences(Collections.singleton(new AssessmentTopicReference(assessmentTopic, RatingLevel.ONE, "reference")));
        assessmentTopic.setTopicId(1);
        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));

        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.createAssessmentTopicReference(topicReferencesRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedTopicNameIsAlreadyPresent() {
        AssessmentTopicRequest assessmentTopicRequest = new AssessmentTopicRequest();
        assessmentTopicRequest.setTopicName("topic");
        assessmentTopicRequest.setModule(1);
        assessmentTopicRequest.setActive(true);
        assessmentTopicRequest.setComments("comments");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        AssessmentTopic assessmentTopic = new AssessmentTopic(1, "topic", assessmentModule, true, "");
        AssessmentTopic assessmentTopic1 = new AssessmentTopic(2, "topic2", assessmentModule, true, "");
        when(moduleService.getModule(assessmentModule.getModuleId())).thenReturn(assessmentModule);
        when(topicService.getTopic(assessmentTopic1.getTopicId())).thenReturn(Optional.of(assessmentTopic1));
        Set<AssessmentTopic> assessmentTopics = new HashSet<>();
        assessmentTopics.add(assessmentTopic);
        assessmentTopics.add(assessmentTopic1);
        assessmentModule.setTopics(assessmentTopics);

        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.updateTopic(2, assessmentTopicRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedParameterNameIsAlreadyPresent() {
        AssessmentParameterRequest assessmentParameterRequest = new AssessmentParameterRequest();
        assessmentParameterRequest.setParameterName("parameter");
        assessmentParameterRequest.setTopic(1);
        assessmentParameterRequest.setActive(true);
        assessmentParameterRequest.setComments("comments");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        AssessmentParameter assessmentParameter = new AssessmentParameter(1, "parameter", null, null, null, true, new Date(), new Date(), "", 1);
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(2, "new parameter", null, null, null, true, new Date(), new Date(), "", 1);

        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));
        when(parameterService.getParameter(assessmentParameter1.getParameterId())).thenReturn(Optional.of(assessmentParameter1));
        Set<AssessmentParameter> parameters = new HashSet<>();
        parameters.add(assessmentParameter);
        parameters.add(assessmentParameter1);
        assessmentTopic.setParameters(parameters);

        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.updateParameter(2, assessmentParameterRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedParameterReferenceIsAlreadyPresent() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference1");
        parameterReferencesRequest.setRating(RatingLevel.TWO);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(assessmentParameter, RatingLevel.ONE, "reference");
        assessmentParameterReference.setReferenceId(1);
        AssessmentParameterReference assessmentParameterReference1 = new AssessmentParameterReference(assessmentParameter, RatingLevel.TWO, "reference1");
        assessmentParameterReference1.setReferenceId(2);
        Set<AssessmentParameterReference> assessmentParameterReferences = new HashSet<>();
        assessmentParameterReferences.add(assessmentParameterReference);
        assessmentParameterReferences.add(assessmentParameterReference1);
        assessmentParameter.setReferences(assessmentParameterReferences);
        assessmentParameter.setParameterId(1);
        when(parameterService.getParameter(assessmentParameter.getParameterId())).thenReturn(Optional.of(assessmentParameter));
        when(parameterService.getAssessmentParameterReference(assessmentParameterReference.getReferenceId())).thenReturn(assessmentParameterReference);

        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.updateParameterReference(1, parameterReferencesRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedTopicReferenceIsAlreadyPresent() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference1");
        topicReferencesRequest.setRating(RatingLevel.TWO);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(assessmentTopic, RatingLevel.ONE, "reference");
        assessmentTopicReference.setReferenceId(1);
        AssessmentTopicReference assessmentTopicReference1 = new AssessmentTopicReference(assessmentTopic, RatingLevel.TWO, "reference1");
        assessmentTopicReference1.setReferenceId(2);
        assessmentTopic.setTopicId(1);
        Set<AssessmentTopicReference> assessmentTopicReferences = new HashSet<>();
        assessmentTopicReferences.add(assessmentTopicReference);
        assessmentTopicReferences.add(assessmentTopicReference1);
        assessmentTopic.setReferences(assessmentTopicReferences);
        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));
        when(topicService.getAssessmentTopicReference(assessmentTopicReference.getReferenceId())).thenReturn(assessmentTopicReference);


        assertThrows(DuplicateRecordException.class, () -> moduleContributorService.updateTopicReference(1, topicReferencesRequest));
    }
}
