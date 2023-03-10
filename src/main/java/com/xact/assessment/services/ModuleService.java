/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ParameterLevelRating;
import com.xact.assessment.models.TopicLevelRating;
import com.xact.assessment.repositories.ModuleRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Singleton
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

    public Integer getAssessedModules(List<TopicLevelRating> topicLevelRatingList, List<ParameterLevelRating> parameterLevelRatingList){
        Set<Integer> assessedModule = new TreeSet<>();
        for (ParameterLevelRating parameterLevelRating : parameterLevelRatingList) {
            assessedModule.add(parameterLevelRating.getParameterLevelId().getParameter().getTopic().getModule().getModuleId());
        }

        for (TopicLevelRating topicLevelRating : topicLevelRatingList) {
            assessedModule.add(topicLevelRating.getTopicLevelId().getTopic().getModule().getModuleId());
        }

        return assessedModule.size();
    }


}

