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
@Table(name = "tbm_assessment_module")
public class AssessmentModule {
    @Id
    @Column(name = "module_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer moduleId;

    @NotNull
    @Column(name = "module_name")
    private String moduleName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category",referencedColumnName="category_id")
    private AssessmentCategory category;

    @OneToMany(mappedBy = "module")
    @ElementCollection()
    private Set<AssessmentTopic> topics;
}
