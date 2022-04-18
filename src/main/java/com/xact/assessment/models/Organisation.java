package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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

    public Organisation() {
    }

    public Organisation(Long organisationId, String organisationName, String industry, String domain, int size) {
        this.organisationId = organisationId;
        this.organisationName = organisationName;
        this.industry = industry;
        this.domain = domain;
        this.size = size;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
