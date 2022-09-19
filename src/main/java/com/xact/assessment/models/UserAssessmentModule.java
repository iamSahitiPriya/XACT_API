package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@EqualsAndHashCode
@Entity
@Table(name="tbl_user_assessment_modules")
public class UserAssessmentModule implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "assessment", column = @Column(name = "assessment"))
    @AttributeOverride(name = "module", column = @Column(name = "module"))
    public AssessmentModuleId assessmentModuleId;


    @NotNull
    @ManyToOne()
    @JoinColumn(name = "assessment", referencedColumnName = "assessment_id",insertable = false,updatable = false)
    private Assessment assessment;

    @NotNull
    @ManyToOne()
    @JoinColumn(name="module", referencedColumnName = "module_id",insertable = false,updatable = false)
    private AssessmentModule module;

}
