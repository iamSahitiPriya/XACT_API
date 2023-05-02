/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import com.xact.assessment.dtos.ContributorRole;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Introspected
@Entity
@Table(name = "tbl_module_contributors")
public class ModuleContributor implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "module", column = @Column(name = "module_id"))
    @AttributeOverride(name = "userEmail", column = @Column(name = "user_email"))
    public ContributorId contributorId;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorRole contributorRole;

    @Column(name = "user_email", nullable = false, insertable = false, updatable = false)
    private String userEmail;

}
