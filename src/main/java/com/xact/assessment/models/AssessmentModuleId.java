package com.xact.assessment.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AssessmentModuleId implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "assessment")
    private Assessment assessment;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "module")
    private AssessmentModule module;


}
