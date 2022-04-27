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
@Table(name = "tbm_assessment_category")
public class AssessmentCategory {
    @Id
    @Column(name = "category_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @NotNull
    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "category")
    @ElementCollection()
    private Set<AssessmentModule> modules;
}
