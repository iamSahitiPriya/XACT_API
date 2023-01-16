package com.xact.assessment.models;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AssessmentModuleId implements Serializable {
    @ManyToOne
    @MapsId
    @JoinColumn(name = "assessment")
    private Assessment assessment;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "module")
    private AssessmentModule module;


}
