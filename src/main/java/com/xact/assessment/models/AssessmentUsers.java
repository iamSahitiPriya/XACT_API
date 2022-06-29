/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

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
@Entity
@EqualsAndHashCode
@Table(name = "tbl_assessment_users")
public class AssessmentUsers implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "userEmail", column = @Column(name = "user_email"))
    @AttributeOverride(name = "assessment", column = @Column(name = "assessment_id"))
    public UserId userId;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssessmentRole role;
}

