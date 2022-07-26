/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"parameters", "references"})
@Introspected
@Entity
@Table(name = "tbm_assessment_topic")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "topicId")
public class AssessmentTopic {
    @Id
    @Column(name = "topic_id", nullable = false, unique = true)
    private Integer topicId;

    @NotNull
    @Column(name = "topic_name")
    private String topicName;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "module", referencedColumnName = "module_id")
    private AssessmentModule module;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topic")
    @ElementCollection()
    private Set<AssessmentParameter> parameters;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "topic")
    @ElementCollection()
    private Set<AssessmentTopicReference> references;

    @NotNull
    @Column(name = "assessment_level")
    @Enumerated(EnumType.STRING)
    private AssessmentLevel assessmentLevel;

    public double getTopicAverage(List<TopicLevelAssessment> topicLevelAssessmentList, List<ParameterLevelAssessment> parameterLevelAssessmentList) {
        double parameterSum = 0;
        int parameterCount = 0;

        if (this.references != null) {
            for (TopicLevelAssessment topicLevelAssessment : topicLevelAssessmentList) {
                if (topicLevelAssessment.getTopicLevelId().getTopic().getTopicId().equals(this.getTopicId())) {
                    return topicLevelAssessment.getRating();
                }
            }
        }
        for (AssessmentParameter assessmentParameter : this.parameters) {
            double parameterAverageScore = assessmentParameter.getParameterAverage(parameterLevelAssessmentList);
            if (parameterAverageScore != 0) {
                parameterSum += parameterAverageScore;
                parameterCount += 1;
            }
        }


        if (parameterSum == 0 && parameterCount == 0) {
            return 0;
        }
        return parameterSum / parameterCount;
    }


}
