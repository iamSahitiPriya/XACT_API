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
public class TopicLevelId implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "topic_id")
    private AssessmentTopic topic_id;
}
