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
public class UserId implements Serializable {

    private String userEmail;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;


}
