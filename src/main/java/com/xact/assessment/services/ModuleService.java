package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.repositories.ModuleRepository;
import jakarta.inject.Singleton;

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
}
