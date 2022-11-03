package com.xact.assessment.models;


import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.Introspected;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Entity
@Table(name = "tbl_notification")
public class EmailNotifier {
    @Id
    @Column(name = "notification_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @NotNull
    @Column(name = "template_name",nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationTemplateType templateName;

    @NotNull
    @Column(name = "user_email",nullable = false)
    private String userEmail;

    @NotNull
    @Column(name= "payload",columnDefinition = "json")
    private String payLoad;

    @NotNull
    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @NotNull
    @Column(name = "retries")
    private Integer retries;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}


