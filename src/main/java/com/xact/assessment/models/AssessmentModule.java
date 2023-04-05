/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "topics")
@Introspected
@Entity
@Table(name = "tbm_assessment_module")
public class AssessmentModule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id", nullable = false, unique = true)
    private Integer moduleId;

    @NotNull
    @Column(name = "module_name")
    private String moduleName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "category_id")
    private AssessmentCategory category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "module")
    @ElementCollection()
    private Set<AssessmentTopic> topics;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "comments")
    private String comments;


    public boolean getIsActive() {
        return isActive;
    }

    public Set<AssessmentTopic> getActiveTopics() {
        return topics == null ? null : topics.stream().filter(AssessmentTopic::getIsActive).collect(Collectors.toSet());
    }

    public double getModuleAverage() {
        double topicSum = 0;
        int topicCount = 0;
        for (AssessmentTopic assessmentTopic : this.topics) {
            double averageTopic = assessmentTopic.getTopicAverage();
            if (averageTopic != 0) {
                topicSum += averageTopic;
                topicCount += 1;
            }
        }

        if (topicCount == 0) {
            return 0;
        }
        return (topicSum / topicCount);
    }


    public AssessmentModule(String moduleName, AssessmentCategory category, boolean isActive, String comments) {
        this.moduleName = moduleName;
        this.category = category;
        this.isActive = isActive;
        this.comments = comments;
    }

    public AssessmentModule(Integer moduleId, String moduleName, AssessmentCategory category, boolean isActive, String comments) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.category = category;
        this.isActive = isActive;
        this.comments = comments;
    }
}
