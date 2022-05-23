/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@EqualsAndHashCode(exclude = "modules")
@Introspected
@Entity
@Table(name = "tbm_assessment_category")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "categoryId")
public class AssessmentCategory {
    @Id
    @Column(name = "category_id", nullable = false, unique = true)
    private Integer categoryId;

    @NotNull
    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    @ElementCollection()
    private Set<AssessmentModule> modules;
}
