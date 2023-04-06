/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Introspected
@Entity
@Table(name = "tbl_user_assessment_question")
public class UserQuestion {

    @Id
    @Column(name = "question_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer questionId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "assessment",referencedColumnName = "assessment_id")
    Assessment assessment;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "parameter",referencedColumnName = "parameter_id")
    AssessmentParameter parameter;

    @NotNull
    @Column(name = "question_text")
    private String question;

    @Column(name = "answer_text")
    private String answer;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @NotNull
    @Column(name = "contribution_status")
    private boolean contributionStatus;

    public boolean hasQuestion() {
        return question != null && !question.isBlank();
    }
}
