/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Introspected
@Entity
@Table(name = "tbm_assessment_question")
public class Question {
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

    public Question(String questionText, AssessmentParameter parameter) {
        this.questionText = questionText;
        this.parameter = parameter;
    }
}
