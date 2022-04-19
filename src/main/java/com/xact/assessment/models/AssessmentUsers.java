package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}

