/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ParameterLevelAssessment;
import com.xact.assessment.models.TopicLevelAssessment;
import com.xact.assessment.repositories.ModuleRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Singleton

public class ModuleService {
    public final ModuleRepository moduleRepository;
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;

    public ModuleService(ModuleRepository moduleRepository, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService) {
        this.moduleRepository = moduleRepository;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
    }
    public AssessmentModule getModule(Integer moduleId){
        return moduleRepository.findByModuleId(moduleId);
    }
    public void createModule(AssessmentModule assessmentModule){
        moduleRepository.save(assessmentModule);
    }
    public void updateModule(AssessmentModule assessmentModule){
        moduleRepository.update(assessmentModule);
    }

    public Long getAssessedModule(List<TopicLevelAssessment> topicLevelAssessmentList, List<ParameterLevelAssessment> parameterLevelAssessmentList){
        Set<Integer> assessedModule = new TreeSet<>();
        for (ParameterLevelAssessment parameterLevelAssessment : parameterLevelAssessmentList) {
            assessedModule.add(parameterLevelAssessment.getParameterLevelId().getParameter().getTopic().getModule().getModuleId());
        }

        for (TopicLevelAssessment topicLevelAssessment:topicLevelAssessmentList) {
            assessedModule.add(topicLevelAssessment.getTopicLevelId().getTopic().getModule().getModuleId());
        }

        return (long) assessedModule.size();
    }

}

