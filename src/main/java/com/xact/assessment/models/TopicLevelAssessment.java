package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Entity
@Table(name = "tbl_assessment_topic")
public class TopicLevelAssessment implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "assessmentTopic", column = @Column(name = "topic_id"))
    @AttributeOverride(name = "assessment", column = @Column(name = "assessment_id"))
    public TopicLevelId topicLevelId;

    @Column(name = "score")
    private Integer rating;

    @Column(name = "recommendation")
    private String recommendation;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}
