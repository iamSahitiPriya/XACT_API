package com.xact.assessment.models;

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
    @Column(name = "question_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @NotNull
    @Column(name = "question_text")
    private String questionText;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "parameter",referencedColumnName = "parameter_id")
    private AssessmentParameter parameter;

}
