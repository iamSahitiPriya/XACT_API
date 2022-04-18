package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Introspected
@Entity
@Table(name = "tbl_assessment_users")

public class AssessmentUsers implements Serializable {

    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "userEmail", column = @Column(name = "user_email")), @AttributeOverride(name = "assessment", column = @Column(name = "assessment_id"))})
    public UserId userId;

    @NotNull
    @Column(name = "first_name", nullable = false, unique = true)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false, unique = true)
    private String lastName;

    @NotNull
    @Column(name = "role", nullable = false, unique = true)
    private String role;


    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AssessmentUsers() {
    }

    public AssessmentUsers(UserId userId, String firstName, String lastName, String role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}

