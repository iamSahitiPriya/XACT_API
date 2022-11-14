package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Introspected
@Entity
@Table(name = "tbl_organisation_name")
public class OrganisationName {

    @Id
    @Column(name = "org_id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orgId;

    @NotNull
    @Column(name = "org_name", nullable = false, unique = true)
    private String organisationName;
}
