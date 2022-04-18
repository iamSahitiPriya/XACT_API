package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    private String assessmentStatus;

    @NotNull
    @Column(name = "created_at", nullable = false, unique = true)
    private Date createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false, unique = true)
    private Date updatedAt;

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssessmentStatus() {
        return assessmentStatus;
    }

    public void setAssessmentStatus(String assessmentStatus) {
        this.assessmentStatus = assessmentStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Assessment() {
    }

    public Assessment(Long assessmentId, String assessmentName, Organisation organisation, String description, String assessmentStatus, Date createdAt, Date updatedAt) {
        this.assessmentId = assessmentId;
        this.assessmentName = assessmentName;
        this.organisation = organisation;
        this.description = description;
        this.assessmentStatus = assessmentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
