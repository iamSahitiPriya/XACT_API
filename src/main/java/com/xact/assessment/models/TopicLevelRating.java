/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class TopicLevelRating extends RatingLevel implements Serializable {

    @EmbeddedId
    @AttributeOverride(name = "assessmentTopic", column = @Column(name = "topic_id"))
    @AttributeOverride(name = "assessment", column = @Column(name = "assessment_id"))
    public TopicLevelId topicLevelId;

    public TopicLevelRating(Integer rating, Date createdAt, Date updatedAt, TopicLevelId topicLevelId) {
        super(rating, createdAt, updatedAt);
        this.topicLevelId = topicLevelId;
    }
}
