/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import com.xact.assessment.dtos.AssessmentStateDto;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

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
    @Column(name = "assessment_purpose", nullable = false)
    private String assessmentPurpose;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organisation")
    private Organisation organisation;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "assessment")
    @ElementCollection()
    private Set<UserAssessmentModule> assessmentModules;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userId.assessment")
    @ElementCollection()
    private Set<AssessmentUser> assessmentUsers;

    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;


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
        return !isDeleted && (assessmentStatus == AssessmentStatus.Active);
    }

    public AssessmentStateDto getAssessmentState() {
        if (assessmentModules != null) {
            return getCategoryStatus() ? AssessmentStateDto.inProgress : AssessmentStateDto.Draft;
        }
        return AssessmentStateDto.Draft;
    }

    public boolean getCategoryStatus() {
        List<AssessmentModule> assessmentModuleList = new ArrayList<>();
        assessmentModules.forEach(assessmentModule -> {
            if (assessmentModule.getModule().getCategory().getIsActive() && assessmentModule.getModule().getIsActive()) {
                assessmentModuleList.add(assessmentModule.getModule());
            }
        });

        return !assessmentModuleList.isEmpty();

    }

    @Transient
    public List<String> getFacilitators() {
        return assessmentUsers.stream().filter(assessmentUsers1 -> assessmentUsers1.getRole() == AssessmentRole.Facilitator).map(assessmentUsers1 -> assessmentUsers1.getUserId().getUserEmail()).toList();
    }

    @Transient
    public String getOwnerEmail() {
        return assessmentUsers.stream().filter(assessmentUsers1 -> assessmentUsers1.getRole() == AssessmentRole.Owner).map(assessmentUsers1 -> assessmentUsers1.getUserId().getUserEmail()).findFirst().orElse("");
    }

    @Transient
    public Optional<AssessmentUser> getOwner() {
        return assessmentUsers.stream().filter(assessmentUsers1 -> assessmentUsers1.getRole() == AssessmentRole.Owner).findFirst();
    }

    public Assessment(Integer assessmentId, String assessmentName, String assessmentPurpose, Organisation organisation, AssessmentStatus assessmentStatus, Date createdAt, Date updatedAt) {
        this.assessmentId = assessmentId;
        this.assessmentName = assessmentName;
        this.assessmentPurpose = assessmentPurpose;
        this.organisation = organisation;
        this.assessmentStatus = assessmentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
