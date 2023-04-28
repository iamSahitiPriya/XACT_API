/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.exceptions.DuplicateRecordException;
import com.xact.assessment.exceptions.InvalidHierarchyException;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ModuleContributorRepository;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class ModuleContributorService {
    private final QuestionService questionService;
    private final  ParameterService parameterService;
    private final TopicService topicService;
    private final ModuleContributorRepository moduleContributorRepository;
    private final ModuleService moduleService;
    private static final String DUPLICATE_RECORDS_ARE_NOT_ALLOWED = "Duplicate records are not allowed";
    private static final String HIERARCHY_NOT_ALLOWED = "Update not allowed because topic and parameter both have associated references.";
    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentMasterDataService.class);



    public ModuleContributorService(QuestionService questionService, ParameterService parameterService, TopicService topicService, ModuleContributorRepository moduleContributorRepository, ModuleService moduleService) {
        this.questionService = questionService;
        this.parameterService = parameterService;
        this.topicService = topicService;
        this.moduleService = moduleService;
        this.moduleContributorRepository = moduleContributorRepository;
    }

    public List<AssessmentModule> getModulesByRole(String userEmail, ContributorRole contributorRole) {
        return moduleContributorRepository.findByRole(userEmail, contributorRole);
    }

    public Set<ModuleContributor> getContributorsByEmail(String userEmail) {
        return moduleContributorRepository.findContributorsByEmail(userEmail);
    }

    public Optional<ContributorRole> getRole(Integer moduleId, String userEmail) {
        return moduleContributorRepository.findRole(moduleId, userEmail);
    }


    public ContributorResponse getContributorResponse(ContributorRole role, String userEmail) {
        List<AssessmentModule> assessmentModules = getModulesByRole(userEmail, role);
        return questionService.getContributorResponse(role,assessmentModules);
    }

    public Question createAssessmentQuestion(String userEmail, QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
        Optional<ContributorRole> contributorRole = getRole(question.getParameter().getTopic().getModule().getModuleId(), userEmail);
        return questionService.createQuestion(contributorRole, question);
    }

    public QuestionStatusUpdateResponse updateContributorQuestionsStatus(Integer moduleId, ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, String userEmail) {
        Optional<ContributorRole> contributorRole = getRole(moduleId, userEmail);
        return  questionService.updateContributorQuestionsStatus(status, questionStatusUpdateRequest, contributorRole);
    }

    public void deleteQuestion(Integer questionId, String userEmail) {
        Question question = questionService.getQuestionById(questionId);
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        Optional<ContributorRole> contributorRole = getRole(moduleId, userEmail);
        questionService.deleteQuestion(question, contributorRole);
    }

    public Question updateContributorQuestion(Integer questionId, String questionText, String userEmail) {
        Question question = questionService.getQuestionById(questionId);
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        Optional<ContributorRole> contributorRole = getRole(moduleId, userEmail);
        return questionService.updateContributorQuestion(question, questionText, contributorRole);
    }

    public void saveContributors(Integer moduleId, List<ContributorDto> contributors) {
        List<ModuleContributor> moduleContributors = new ArrayList<>();
        for (ContributorDto contributor : contributors) {
            ModuleContributor moduleContributor = getModuleContributor(moduleId, contributor);
            moduleContributors.add(moduleContributor);
        }
        moduleContributorRepository.deleteByModuleId(moduleId);
        moduleContributorRepository.saveAll(moduleContributors);
    }
    private ModuleContributor getModuleContributor(Integer moduleId, ContributorDto contributorDto) {
        ContributorId contributorId = new ContributorId();
        contributorId.setModule(moduleService.getModule(moduleId));
        contributorId.setUserEmail(contributorDto.getUserEmail());
        ModuleContributor moduleContributor = new ModuleContributor();
        moduleContributor.setContributorId(contributorId);
        moduleContributor.setContributorRole(contributorDto.getRole());
        return moduleContributor;
    }
    public AssessmentTopic updateTopic(Integer topicId, AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        if (isUnique(assessmentTopic.getTopicName(), assessmentTopicRequest.getTopicName(), isTopicUnique(assessmentTopicRequest.getTopicName(), assessmentModule))) {
            assessmentTopic.setModule(assessmentModule);
            assessmentTopic.setTopicName(assessmentTopicRequest.getTopicName());
            assessmentTopic.setActive(assessmentTopicRequest.isActive());
            assessmentTopic.setComments(assessmentTopicRequest.getComments());
            assessmentTopic.setUpdatedAt(new Date());
            topicService.updateTopic(assessmentTopic);
            return assessmentTopic;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    public AssessmentParameter updateParameter(Integer parameterId, AssessmentParameterRequest assessmentParameterRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameterRequest.getTopic()).orElseThrow();
        if (isUnique(assessmentParameter.getParameterName(), assessmentParameterRequest.getParameterName(), isParameterUnique(assessmentParameterRequest.getParameterName(), assessmentTopic))) {

            if (assessmentParameter.hasReferences() && assessmentTopic.hasReferences()) {
                LOGGER.info(HIERARCHY_NOT_ALLOWED);
                throw new InvalidHierarchyException(HIERARCHY_NOT_ALLOWED);
            }

            assessmentParameter.setParameterName(assessmentParameterRequest.getParameterName());
            assessmentParameter.setTopic(assessmentTopic);
            assessmentParameter.setActive(assessmentParameterRequest.isActive());
            assessmentParameter.setComments(assessmentParameterRequest.getComments());
            parameterService.updateParameter(assessmentParameter);
            return assessmentParameter;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }
    private boolean isUnique(String parameterName, String parameterName2, boolean isOtherDataUnique) {
        return removeWhiteSpaces(parameterName).equals(removeWhiteSpaces(parameterName2)) || isOtherDataUnique;
    }
    private boolean isUnique(List<String> actualList, String name) {
        List<String> result = actualList.stream()
                .map(String::toLowerCase).map(option -> option.replaceAll("\\s", ""))
                .toList();
        return !(result.contains(name.toLowerCase().replaceAll("\\s", "")));
    }

    public Question updateQuestion(Integer questionId, QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = questionService.getQuestion(questionId).orElseThrow();
        question.setQuestionText(questionRequest.getQuestionText());
        question.setParameter(assessmentParameter);

        questionService.updateQuestion(question);
        return question;


    }

    public AssessmentTopicReference updateTopicReference(Integer referenceId, TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        AssessmentTopicReference assessmentTopicReference = topicService.getAssessmentTopicReference(referenceId);
        Set<AssessmentTopicReference> assessmentTopicReferences = new HashSet<>(assessmentTopic.getReferences().stream().filter(reference -> !Objects.equals(reference.getReferenceId(), assessmentTopicReference.getReferenceId())).toList());
        if (isTopicRatingUnique(assessmentTopicReferences, topicReferencesRequest.getRating()) && isTopicReferenceUnique(assessmentTopicReferences, topicReferencesRequest.getReference())) {
            assessmentTopicReference.setReference(topicReferencesRequest.getReference());
            assessmentTopicReference.setTopic(assessmentTopic);
            assessmentTopicReference.setRating(topicReferencesRequest.getRating());
            return topicService.updateTopicReference(assessmentTopicReference);
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    public AssessmentParameterReference updateParameterReference(Integer referenceId, ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        AssessmentParameterReference assessmentParameterReference = parameterService.getAssessmentParameterReference(referenceId);
        Set<AssessmentParameterReference> assessmentParameterReferences = new HashSet<>(assessmentParameter.getReferences().stream().filter(reference -> !Objects.equals(reference.getReferenceId(), assessmentParameterReference.getReferenceId())).toList());
        if (isParameterRatingUnique(assessmentParameterReferences, parameterReferencesRequest.getRating()) && isParameterReferenceUnique(assessmentParameterReferences, parameterReferencesRequest.getReference())) {
            assessmentParameterReference.setParameter(assessmentParameter);
            assessmentParameterReference.setReference(parameterReferencesRequest.getReference());
            assessmentParameterReference.setRating(parameterReferencesRequest.getRating());

            parameterService.updateParameterReference(assessmentParameterReference);
            return assessmentParameterReference;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    public void deleteTopicReference(Integer referenceId) {
        topicService.deleteTopicReference(referenceId);
    }

    public void deleteParameterReference(Integer referenceId) {
        parameterService.deleteParameterReference(referenceId);
    }
    public AssessmentTopic createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        if (isTopicUnique(assessmentTopicRequest.getTopicName(), assessmentModule)) {
            AssessmentTopic assessmentTopic = new AssessmentTopic(assessmentTopicRequest.getTopicName(), assessmentModule, assessmentTopicRequest.isActive(), assessmentTopicRequest.getComments());
            topicService.createTopic(assessmentTopic);
            return assessmentTopic;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }
    }

    private boolean isTopicUnique(String topicName, AssessmentModule assessmentModule) {
        List<String> topics = assessmentModule.getTopics().stream().map(AssessmentTopic::getTopicName).toList();
        return isUnique(topics, topicName);
    }


    public AssessmentParameter createAssessmentParameter(AssessmentParameterRequest assessmentParameter) {
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameter.getTopic()).orElseThrow();
        if (isParameterUnique(assessmentParameter.getParameterName(), assessmentTopic)) {
            AssessmentParameter assessmentParameter1 = AssessmentParameter.builder().parameterName(assessmentParameter.getParameterName()).topic(assessmentTopic)
                    .isActive(assessmentParameter.isActive()).comments(assessmentParameter.getComments()).build();

            parameterService.createParameter(assessmentParameter1);
            return assessmentParameter1;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }

    }
    private boolean isParameterUnique(String parameterName, AssessmentTopic assessmentTopic) {
        List<String> parameters = assessmentTopic.getParameters().stream().map(AssessmentParameter::getParameterName).toList();
        return isUnique(parameters, parameterName);
    }

    public AssessmentTopicReference createAssessmentTopicReference(TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        if (isTopicRatingUnique(assessmentTopic.getReferences(), topicReferencesRequest.getRating()) && isTopicReferenceUnique(assessmentTopic.getReferences(), topicReferencesRequest.getReference())) {
            AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(assessmentTopic, topicReferencesRequest.getRating(), topicReferencesRequest.getReference());
            topicService.saveTopicReference(assessmentTopicReference);
            return assessmentTopicReference;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    private boolean isTopicRatingUnique(Set<AssessmentTopicReference> references, Rating rating) {
        if (references != null) {
            List<Rating> ratings = references.stream().map(AssessmentTopicReference::getRating).toList();
            return !ratings.contains(rating);
        } else return true;
    }

    public AssessmentParameterReference createAssessmentParameterReference(ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        if (isParameterRatingUnique(assessmentParameter.getReferences(), parameterReferencesRequest.getRating()) && isParameterReferenceUnique(assessmentParameter.getReferences(), parameterReferencesRequest.getReference())) {
            AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(assessmentParameter, parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());
            parameterService.saveParameterReference(assessmentParameterReference);
            return assessmentParameterReference;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }
    private boolean isParameterReferenceUnique(Set<AssessmentParameterReference> references, String reference) {
        if (references != null) {
            List<String> parameterReferences = references.stream().map(AssessmentParameterReference::getReference).toList();
            return isUnique(parameterReferences, reference);
        } else return true;
    }

    private boolean isParameterRatingUnique(Set<AssessmentParameterReference> references, Rating rating) {
        if (references != null) {
            List<Rating> ratings = references.stream().map(AssessmentParameterReference::getRating).toList();
            return !ratings.contains(rating);
        } else return true;
    }
    private String removeWhiteSpaces(String name) {
        return name.toLowerCase().replaceAll("\\s", "");
    }
    private boolean isTopicReferenceUnique(Set<AssessmentTopicReference> references, String reference) {
        if (references != null) {
            List<String> topicReferences = references.stream().map(AssessmentTopicReference::getReference).toList();
            return isUnique(topicReferences, reference);
        } else return true;
    }

    public boolean validate(User loggedInUser, AssessmentModule module) {
        Optional<ContributorRole> role = moduleContributorRepository.findRole(module.getModuleId(), loggedInUser.getUserEmail());
        return role.isPresent() && role.get().equals(ContributorRole.AUTHOR);

    }

    public AssessmentModule getModule(Integer topic) {
        return topicService.getTopicById(topic).getModule();
    }

    public AssessmentModule getModuleById(Integer module) {
        return moduleService.getModule(module);
    }

    public AssessmentModule getModuleByParameter(Integer parameterId) {
        Optional<AssessmentParameter> parameter =  parameterService.getParameter(parameterId);
        return parameter.isPresent()? parameter.get().getTopic().getModule():new AssessmentModule();
    }

    public AssessmentModule getModuleByTopicReference(Integer referenceId) {
        AssessmentTopicReference assessmentTopicReference = topicService.getAssessmentTopicReference(referenceId);
        return assessmentTopicReference.getTopic().getModule();
    }

    public AssessmentModule getModuleByParameterReference(Integer referenceId) {
        AssessmentParameterReference assessmentParameterReference = parameterService.getAssessmentParameterReference(referenceId);
        return assessmentParameterReference.getParameter().getTopic().getModule();
    }
}
