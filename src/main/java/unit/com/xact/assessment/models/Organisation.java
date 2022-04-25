package unit.com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Introspected
@Entity
@Table(name = "tbl_organisation")
public class Organisation {

    @Id
    @Column(name = "organisation_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
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
