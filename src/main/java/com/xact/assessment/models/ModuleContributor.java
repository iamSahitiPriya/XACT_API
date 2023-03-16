package com.xact.assessment.models;

import com.xact.assessment.dtos.ContributorQuestionStatus;
import com.xact.assessment.dtos.ContributorRole;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Introspected
@Entity
@Table(name = "tbl_module_contributors")
public class ModuleContributor implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "module", column = @Column(name = "module_id"))
    @AttributeOverride(name = "user_name", column = @Column(name = "user_name"))
    public ContributorId contributorId;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorRole contributorRole;

}
