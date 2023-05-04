/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbm_assessment_question_reference")
public class AssessmentQuestionReference  extends Reference{
    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "question", referencedColumnName = "question_id")
    private Question question;

    public AssessmentQuestionReference(Rating rating, String reference, Question question) {
        super(rating, reference);
        this.question = question;
    }
}
