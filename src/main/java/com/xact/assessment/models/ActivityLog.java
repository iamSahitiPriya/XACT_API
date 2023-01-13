/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import com.xact.assessment.dtos.ActivityType;
import io.micronaut.core.annotation.Introspected;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@EqualsAndHashCode
@Entity
@Table(name = "tbl_activity_log")
public class ActivityLog {
    @EmbeddedId
    @AttributeOverride(name = "user_name", column = @Column(name = "user_name"))
    @AttributeOverride(name = "assessment", column = @Column(name = "assessment_id"))
    private ActivityId activityId;


    @NotNull
    @ManyToOne()
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private AssessmentTopic topic;

    @NotNull
    @Column(name = "id")
    private Integer identifier;

    @Column(name = "activity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;


    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
