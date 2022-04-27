package com.xact.assessment.models;

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
@Entity
@Table(name = "tbm_assessment_topic_reference")
public class AssessmentTopicReference {

    @Id
    @Column(name = "reference_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer referenceId;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "topic",referencedColumnName = "topic_id")
    private AssessmentTopic topic;

    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "reference")
    private String reference;
}
