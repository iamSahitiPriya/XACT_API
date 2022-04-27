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
@Table(name = "tbm_assessment_topic")
public class AssessmentTopic {
    @Id
    @Column(name = "topic_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer topicId;

    @NotNull
    @Column(name = "topic_name")
    private String topicName;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "module", referencedColumnName = "module_id")
    private AssessmentModule module;

    @OneToMany(mappedBy = "topic")
    @ElementCollection()
    private Set<AssessmentParameter> parameters;

    @OneToMany(mappedBy = "topic")
    @ElementCollection()
    private Set<AssessmentTopicReference> references;
}
