/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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
}
