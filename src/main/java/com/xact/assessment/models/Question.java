/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xact.assessment.dtos.ContributorQuestionStatus;
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
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Introspected
@Entity
@Table(name = "tbm_assessment_question")
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", nullable = false, unique = true)
    private Integer questionId;

    @NotNull
    @Column(name = "question_text")
    private String questionText;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "parameter", referencedColumnName = "parameter_id")
    private AssessmentParameter parameter;

    @Column(name = "question_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorQuestionStatus questionStatus;

    @Column(name = "comments")
    private String comments;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public Question(String questionText, AssessmentParameter parameter) {
        this.questionText = questionText;
        this.parameter = parameter;
    }


    @Transient
    static
    EnumMap<ContributorQuestionStatus, Set<ContributorQuestionStatus>> lifeCycleMap = new EnumMap<>(ContributorQuestionStatus.class);


    static {

        Set<ContributorQuestionStatus> draftNextState = new HashSet<>();
        draftNextState.add(ContributorQuestionStatus.SENT_FOR_REVIEW);
        lifeCycleMap.put(ContributorQuestionStatus.DRAFT, draftNextState);

        Set<ContributorQuestionStatus> requestForChangeNextState = new HashSet<>();
        requestForChangeNextState.add(ContributorQuestionStatus.SENT_FOR_REVIEW);
        lifeCycleMap.put(ContributorQuestionStatus.REQUESTED_FOR_CHANGE, requestForChangeNextState);

        Set<ContributorQuestionStatus> sentForReviewNextState = new HashSet<>();
        sentForReviewNextState.add(ContributorQuestionStatus.REQUESTED_FOR_CHANGE);
        sentForReviewNextState.add(ContributorQuestionStatus.PUBLISHED);
        sentForReviewNextState.add(ContributorQuestionStatus.REJECTED);
        lifeCycleMap.put(ContributorQuestionStatus.SENT_FOR_REVIEW, sentForReviewNextState);

    }

    public boolean isNextStatusAllowed(ContributorQuestionStatus nextStatus) {
        return lifeCycleMap.get(this.questionStatus).contains(nextStatus);
    }


}
