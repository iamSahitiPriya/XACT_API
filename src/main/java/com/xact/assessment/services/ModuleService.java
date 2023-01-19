/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ParameterLevelAssessment;
import com.xact.assessment.models.TopicLevelAssessment;
import com.xact.assessment.repositories.ModuleRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Singleton
@Transactional
public class ModuleService {
    public final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
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

    public Integer getAssessedModule(List<TopicLevelAssessment> topicLevelAssessmentList, List<ParameterLevelAssessment> parameterLevelAssessmentList){
        Set<Integer> assessedModule = new TreeSet<>();
        for (ParameterLevelAssessment parameterLevelAssessment : parameterLevelAssessmentList) {
            assessedModule.add(parameterLevelAssessment.getParameterLevelId().getParameter().getTopic().getModule().getModuleId());
        }

        for (TopicLevelAssessment topicLevelAssessment:topicLevelAssessmentList) {
            assessedModule.add(topicLevelAssessment.getTopicLevelId().getTopic().getModule().getModuleId());
        }

        return assessedModule.size();
    }

}

