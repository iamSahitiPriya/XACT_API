package com.xact.assessment.models;


import com.vladmihalcea.hibernate.type.json.JsonType;
import io.micronaut.core.annotation.Introspected;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Entity
@Table(name = "tbl_notification")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
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
    @Type(type="json")
    private HashMap<String,String> payload;

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


