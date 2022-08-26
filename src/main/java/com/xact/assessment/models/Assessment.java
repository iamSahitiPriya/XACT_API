/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode
@Introspected
@Entity
@Table(name = "tbl_assessment")
public class Assessment {

    @Id
    @Column(name = "assessment_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assessmentId;

    @NotNull
    @Column(name = "assessment_name", nullable = false, unique = true)
    private String assessmentName;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organisation")
    private Organisation organisation;

    @NotNull
    @Column(name = "assessment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssessmentStatus assessmentStatus;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assessment that = (Assessment) o;
        return assessmentId.equals(that.assessmentId) &&
                assessmentName.equals(that.assessmentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, assessmentName);
    }


    public boolean isEditable() {
        return assessmentStatus == AssessmentStatus.Active;
    }
}
