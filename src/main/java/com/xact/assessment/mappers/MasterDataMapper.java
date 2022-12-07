/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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

    static {
        PropertyMap<AssessmentModule, AssessmentModuleDto> moduleMapOnly = new PropertyMap<>() {
            protected void configure() {
                map().setCategory(source.getCategory().getCategoryId());
                map().setTopics(new TreeSet<>());
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
        mapper.addMappings(moduleMap);
        mapper.addMappings(topicMap);
        mapper.addMappings(parameterMap);
        mapper.addMappings(questionMap);
        mapper.addMappings(topicReferenceMap);
        mapper.addMappings(parameterReferenceMap);
        moduleMapper.addMappings(moduleMapOnly);
    }

    public AssessmentCategoryDto mapTillModuleOnly(AssessmentCategory assessmentCategory) {
        return moduleMapper.map(assessmentCategory, AssessmentCategoryDto.class);
    }

    public AssessmentCategoryDto map(AssessmentCategory assessmentCategory) {
        return mapper.map(assessmentCategory, AssessmentCategoryDto.class);
    }

    public CategoryDto mapCategory(AssessmentCategory assessmentCategory) {
        return mapper.map(assessmentCategory, CategoryDto.class);
    }
}
