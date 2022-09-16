package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@EqualsAndHashCode
@Entity
@Table(name="tbl_user_assessment_modules")
public class UserAssessmentModule {

    @Id
    @Column(name="id",unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "assessment", referencedColumnName = "assessment_id")
    private Assessment assessment;

    @ManyToOne()
    @JoinColumn(name="module", referencedColumnName = "module_id")
    private AssessmentModule module;

}
