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
public class ActivityId implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "assessment")
    private Assessment assessment;

    private String userName;


}
