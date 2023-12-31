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
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Entity
@Table(name = "tbl_assessment_question")
public class Answer implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "question", column = @Column(name = "question_id"))
    @AttributeOverride(name = "assessment", column = @Column(name = "assessment_id"))
    public AnswerId answerId;


    @Column(name = "notes")
    private String answerNote;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name="score")
    private Integer rating;


    public boolean hasNotes() {
        return answerNote != null && !answerNote.isBlank();
    }

    public boolean hasRating() {
        return rating != null;
    }
}
