/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


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
@Table(name = "tbl_notification")
public class Notification {
    @Id
    @Column(name = "notification_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @NotNull
    @Column(name = "template_name",nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType templateName;

    @NotNull
    @Column(name = "user_email",nullable = false)
    private String userEmail;

    @NotNull
    @Column(name= "payload")
    private String payload;

    @NotNull
    @Column(name="status",nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;


    @Column(name = "retries",nullable = false)
    private Integer retries = 0;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

}


