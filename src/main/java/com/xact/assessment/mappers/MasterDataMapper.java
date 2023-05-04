/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.mappers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.TreeSet;

public class MasterDataMapper {

    private static final ModelMapper moduleMapper = new ModelMapper();
    private static final ModelMapper mapper = new ModelMapper();
    private static final ModelMapper contributorModuleMapper = new ModelMapper();
    private static final ModelMapper contributorTopicMapper = new ModelMapper();
    private static final ModelMapper contributorParameterMapper = new ModelMapper();
    private static final ModelMapper questionMapper = new ModelMapper();

    static {
        PropertyMap<AssessmentModule, AssessmentModuleDto> moduleMapOnly = new PropertyMap<>() {
            protected void configure() {
                map().setCategory(source.getCategory().getCategoryId());
                map().setTopics(new TreeSet<>());
            }
        };
        PropertyMap<AssessmentModule,AssessmentModuleDto> contributorModuleMap = new PropertyMap<>() {
            protected void configure() {
                map().setCategory(source.getCategory().getCategoryId());
                map().setTopics(new TreeSet<>());
            }
        };
        PropertyMap<AssessmentTopic,AssessmentTopicDto> contributorTopicMap = new PropertyMap<>() {
            protected void configure() {
                map().setModule(source.getModule().getModuleId());
                map().setParameters(new TreeSet<>());
            }
        };
        PropertyMap<AssessmentParameter,AssessmentParameterDto> contributorParameterMap = new PropertyMap<>() {
            protected void configure() {
                map().setTopic(source.getTopic().getTopicId());
                map().setQuestions(new TreeSet<>());
            }
        };
        PropertyMap<AssessmentModule, AssessmentModuleDto> moduleMap = new PropertyMap<>() {
            protected void configure() {
                map().setCategory(source.getCategory().getCategoryId());
            }
        };
        PropertyMap<AssessmentTopic, AssessmentTopicDto> topicMap = new PropertyMap<>() {
            protected void configure() {
                map().setModule(source.getModule().getModuleId());
            }
        };
        PropertyMap<AssessmentParameter, AssessmentParameterDto> parameterMap = new PropertyMap<>() {
            protected void configure() {
                map().setTopic(source.getTopic().getTopicId());
            }
        };
        PropertyMap<Question, QuestionDto> questionMap = new PropertyMap<>() {
            protected void configure() {
                map().setParameter(source.getParameter().getParameterId());
            }
        };
        PropertyMap<AssessmentTopicReference, AssessmentTopicReferenceDto> topicReferenceMap = new PropertyMap<>() {
            protected void configure() {
                map().setTopic(source.getTopic().getTopicId());
            }
        };
        PropertyMap<AssessmentParameterReference, AssessmentParameterReferenceDto> parameterReferenceMap = new PropertyMap<>() {
            protected void configure() {
                map().setParameter(source.getParameter().getParameterId());
            }
        };
        PropertyMap<AssessmentQuestionReference,AssessmentQuestionReferenceDto> questionReferenceMap = new PropertyMap<>() {
            protected void configure() {
                map().setQuestion(source.getQuestion().getQuestionId());
            }
        };
        mapper.addMappings(moduleMap);
        mapper.addMappings(topicMap);
        mapper.addMappings(parameterMap);
        mapper.addMappings(questionMap);
        mapper.addMappings(topicReferenceMap);
        mapper.addMappings(parameterReferenceMap);
        mapper.addMappings(questionReferenceMap);
        moduleMapper.addMappings(moduleMapOnly);
        contributorModuleMapper.addMappings(contributorModuleMap);
        contributorTopicMapper.addMappings(contributorTopicMap);
        contributorTopicMapper.addMappings(topicReferenceMap);
        contributorParameterMapper.addMappings(contributorParameterMap);
        contributorParameterMapper.addMappings(parameterReferenceMap);
        questionMapper.addMappings(questionMap);
    }


    public AssessmentCategoryDto mapTillModuleOnly(AssessmentCategory assessmentCategory) {
        return moduleMapper.map(assessmentCategory, AssessmentCategoryDto.class);
    }

    public AssessmentCategoryDto mapAssessmentCategory(AssessmentCategory assessmentCategory) {
        return mapper.map(assessmentCategory, AssessmentCategoryDto.class);
    }
    public AssessmentTopicReferenceDto mapTopicReference(AssessmentTopicReference assessmentTopicReference){
        return mapper.map(assessmentTopicReference, AssessmentTopicReferenceDto.class);
    }
    public AssessmentParameterReferenceDto mapParameterReference(AssessmentParameterReference assessmentParameterReference){
        return mapper.map(assessmentParameterReference,AssessmentParameterReferenceDto.class);
    }

    public AssessmentQuestionReferenceDto mapQuestionReference(AssessmentQuestionReference assessmentQuestionReference){
        return mapper.map(assessmentQuestionReference,AssessmentQuestionReferenceDto.class);
    }

    public CategoryDto mapCategory(AssessmentCategory assessmentCategory) {
        return mapper.map(assessmentCategory, CategoryDto.class);
    }

    public AssessmentModuleDto mapModule(AssessmentModule assessmentModule) {
        return contributorModuleMapper.map(assessmentModule, AssessmentModuleDto.class);
    }

    public AssessmentTopicDto mapTopic(AssessmentTopic assessmentTopic) {
        return contributorTopicMapper.map(assessmentTopic,AssessmentTopicDto.class);
    }

    public AssessmentParameterDto mapParameter(AssessmentParameter assessmentParameter) {
        return contributorParameterMapper.map(assessmentParameter,AssessmentParameterDto.class);
    }

    public QuestionDto mapQuestion(Question question) {
        return questionMapper.map(question,QuestionDto.class);
    }
}
