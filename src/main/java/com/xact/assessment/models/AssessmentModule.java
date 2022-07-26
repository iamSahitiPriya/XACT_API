/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "topics")
@Introspected
@Entity
@Table(name = "tbm_assessment_module")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "moduleId")
public class AssessmentModule {
    @Id
    @Column(name = "module_id", nullable = false, unique = true)
    private Integer moduleId;

    @NotNull
    @Column(name = "module_name")
    private String moduleName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "category_id")
    private AssessmentCategory category;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "module")
    @ElementCollection()
    private Set<AssessmentTopic> topics;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive;

    public double getModuleAverage(List<TopicLevelAssessment> topicLevelAssessmentList,List<ParameterLevelAssessment> parameterLevelAssessmentList) {
        double topicSum = 0;
        int topicCount = 0;
        for(AssessmentTopic assessmentTopic : this.topics){
            if(assessmentTopic.getTopicAverage(topicLevelAssessmentList,parameterLevelAssessmentList) != 0){
                topicSum += assessmentTopic.getTopicAverage(topicLevelAssessmentList,parameterLevelAssessmentList);
                topicCount += 1;
            }
        }

        if(topicCount ==0){
            return 0;
        }
        return topicSum/topicCount;
    }
}
