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

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Introspected
@Entity
@Table(name = "tbm_assessment_question")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "questionId")
public class Question {
    @Id
    @Column(name = "question_id", nullable = false, unique = true)
    private Integer questionId;

    @NotNull
    @Column(name = "question_text")
    private String questionText;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "parameter",referencedColumnName = "parameter_id")
    private AssessmentParameter parameter;

}
