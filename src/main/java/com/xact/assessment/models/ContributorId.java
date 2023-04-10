/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ContributorId  implements Serializable {
    @ManyToOne
    @MapsId
    @JoinColumn(name = "module", referencedColumnName = "module_id")
    private AssessmentModule module;

    private String userEmail;
}
