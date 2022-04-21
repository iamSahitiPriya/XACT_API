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
@Table(name = "tbl_organisation")
public class Organisation {

    @Id
    @Column(name = "organisation_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long organisationId;

    @NotNull
    @Column(name = "organisation_name", nullable = false)
    private String organisationName;

    @NotNull
    @Column(name = "industry", nullable = false)
    private String industry;

    @NotNull
    @Column(name = "domain", nullable = false)
    private String domain;

    @NotNull
    @Column(name = "size", nullable = false)
    private int size;
}
