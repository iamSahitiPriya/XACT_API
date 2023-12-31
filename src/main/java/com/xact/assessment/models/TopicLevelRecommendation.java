/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_assessment_topic_recommendation")
public class TopicLevelRecommendation extends Recommendation {
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private AssessmentTopic topic;

}
