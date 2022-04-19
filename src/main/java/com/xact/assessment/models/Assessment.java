package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Entity
@Table(name = "tbl_assessment")
public class Assessment {
    @NotNull
    @Id
    @Column(name = "assessment_id", nullable = false, unique = true)
    private Long assessmentId;

    @NotNull
    @Column(name = "assessment_name", nullable = false, unique = true)
    private String assessmentName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organisation")
    private Organisation organisation;

    @NotNull
    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @NotNull
    @Column(name = "assessment_status", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private AssessmentStatus assessmentStatus;

    @NotNull
    @Column(name = "created_at", nullable = false, unique = true)
    private Date createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false, unique = true)
    private Date updatedAt;
}
