package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Introspected
@Entity

@Table(name = "tbl_assessment")
public class AssessmentUserDTO {

    @NotNull
    @Id
    @Column(name = "assessment_id", nullable = false, unique = true)
    private Long assessmentId;

    @NotNull
    @Column(name = "assessment_name", nullable = false, unique = true)
    private String assessmentName;
    @NotNull
    @Column(name = "assessment_status", nullable = false, unique = true)
    private String assessmentStatus;

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserId userId;
    public AssessmentUserDTO(Long assessmentId, String assessmentName, String assessmentStatus) {
        this.assessmentId = assessmentId;
        this.assessmentName = assessmentName;
        this.assessmentStatus = assessmentStatus;
    }
    public AssessmentUserDTO() {

    }

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


}
