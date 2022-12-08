/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "modules")
@Introspected
@Entity
@Table(name = "tbm_assessment_category")
public class AssessmentCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "comments")
    private String comments;



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

    public AssessmentCategory( String categoryName, boolean isActive, String comments) {
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.comments = comments;
    }

    public AssessmentCategory( Integer categoryId,String categoryName, boolean isActive, String comments) {
        this.categoryId=categoryId;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.comments = comments;
    }
}
