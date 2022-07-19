package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AverageCategoryService {
    private final ModuleRepository moduleRepository;
    private final AssessmentTopicRepository assessmentTopicRepository;
    private final AssessmentParameterRepository assessmentParameterRepository;
    private final ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private final TopicLevelAssessmentRepository topicLevelAssessmentRepository;

    public AverageCategoryService(ModuleRepository moduleRepository, AssessmentTopicRepository assessmentTopicRepository, AssessmentParameterRepository assessmentParameterRepository, ParameterLevelAssessmentRepository parameterLevelAssessmentRepository, TopicLevelAssessmentRepository topicLevelAssessmentRepository) {
        this.moduleRepository = moduleRepository;
        this.assessmentTopicRepository = assessmentTopicRepository;
        this.assessmentParameterRepository = assessmentParameterRepository;
        this.parameterLevelAssessmentRepository = parameterLevelAssessmentRepository;
        this.topicLevelAssessmentRepository = topicLevelAssessmentRepository;
    }

    public double getAverage(Integer categoryIndex, Assessment assessment){
        List<AssessmentModule> assessmentModuleList = moduleRepository.findByCategory(categoryIndex);
        double averageModule;
        int moduleSum = 0;
        int moduleCount=0;
        for(AssessmentModule module: assessmentModuleList){
            List<AssessmentTopic> assessmentTopicList = assessmentTopicRepository.findByModule(module.getModuleId());
            double averageTopic;
            int topicSum = 0;
            int topicCount = 0;
            for(AssessmentTopic assessmentTopic:assessmentTopicList){
                TopicLevelId topicLevelId = new TopicLevelId(assessment,assessmentTopic);
                if(topicLevelAssessmentRepository.findById(topicLevelId).isPresent()){
                    topicSum += topicLevelAssessmentRepository.findById(topicLevelId).get().getRating();
                    topicCount += 1;
                }
                else{
                    List<AssessmentParameter> assessmentParameterList = assessmentParameterRepository.findByTopic(assessmentTopic.getTopicId());
                    int averageParameter;
                    int parameterSum = 0;
                    int parameterCount =0;
                    for(AssessmentParameter assessmentParameter:assessmentParameterList){
                        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment,assessmentParameter);
                        if(parameterLevelAssessmentRepository.findById(parameterLevelId).isPresent()) {
                            parameterSum += parameterLevelAssessmentRepository.findById(parameterLevelId).get().getRating();
                            parameterCount += 1;
                        }
                    }
                    if(parameterSum == 0 && parameterCount ==0){
                        averageParameter = 0;
                    }
                    else {
                        averageParameter = Math.abs(parameterSum / parameterCount);
                        topicSum += averageParameter;
                        topicCount += 1;
                    }
                }

            }
            if(topicSum == 0 && topicCount == 0 ){
                averageTopic = 0;
            }
            else{
                averageTopic = Math.abs(topicSum/topicCount);
                moduleSum += averageTopic;
                moduleCount += 1;
            }
        }
        if(moduleCount == 0 && moduleSum == 0){
            averageModule = 0;
        }
        else{
            averageModule = Math.abs(moduleSum/moduleCount);
        }
        return averageModule;
    }
}
