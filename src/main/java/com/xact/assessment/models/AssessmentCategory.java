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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @NotNull
    @Column(name = "is_active")
    private boolean isActive;

    public Set<AssessmentModule> getModules() {
        return modules == null ? null : modules.stream().filter(AssessmentModule::getIsActive).collect(Collectors.toSet());

    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getCategoryAverage(){
        double moduleSum = 0;
        int moduleCount = 0;
        for(AssessmentModule assessmentModule: this.modules){
            double averageModule = assessmentModule.getModuleAverage();
            if(averageModule != 0){
                moduleSum += averageModule;
                moduleCount +=1 ;
            }
        }
        if(moduleCount == 0){
            return 0;
        }
        return moduleSum/moduleCount;
    }

    public AssessmentCategory(Integer categoryId, String categoryName, boolean isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isActive = isActive;
    }
}
