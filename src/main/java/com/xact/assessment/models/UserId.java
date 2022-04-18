package com.xact.assessment.models;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class UserId implements Serializable {

    private String userEmail;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public UserId() {
    }

    public UserId(String userEmail, Assessment assessment) {
        this.userEmail = userEmail;
        this.assessment = assessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(userEmail, userId.userEmail) && Objects.equals(assessment, userId.assessment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, assessment);
    }
}
