package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Introspected
@Entity
@Table(name = "tbm_assessment_parameter")
public class AssessmentParameter {
    @Id
    @Column(name = "parameter_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parameterId;

    @NotNull
    @Column(name = "parameter_name")
    private String parameterName;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "topic",referencedColumnName = "topic_id")
    private AssessmentTopic topic;

    @OneToMany(mappedBy = "parameter")
    @ElementCollection()
    private Set<Question> questions;

    @OneToMany(mappedBy = "parameter")
    @ElementCollection()
    private Set<AssessmentParameterReference> references;
}
