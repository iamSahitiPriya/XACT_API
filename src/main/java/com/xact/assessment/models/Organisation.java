package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Entity
@Table(name = "tbl_organisation")
public class Organisation {

    @NotNull
    @Id
    @Column(name = "organisation_id", nullable = false, unique = true)
    private Long organisationId;

    @NotNull
    @Column(name = "organisation_name", nullable = false, unique = true)
    private String organisationName;

    @NotNull
    @Column(name = "industry", nullable = false, unique = true)
    private String industry;

    @NotNull
    @Column(name = "domain", nullable = false, unique = true)
    private String domain;

    @NotNull
    @Column(name = "size", nullable = false, unique = true)
    private int size;
}
