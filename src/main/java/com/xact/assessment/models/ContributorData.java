package com.xact.assessment.models;


import com.xact.assessment.dtos.ContributorQuestionStatus;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Introspected
@Entity
@Table(name = "tbl_contributors_data")
public class ContributorData {
    @Id
    @Column(name = "question_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer questionId;

    @NotNull
    @Column(name = "question_text")
    private String question;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "parameter",referencedColumnName = "parameter_id")
    AssessmentParameter parameter;

    @Column(name = "question_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorQuestionStatus questionStatus;

    @Column(name = "author_comments")
    private String authorComments;

    @Column(name = "reviewer_comments")
    private String reviewerComments;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
