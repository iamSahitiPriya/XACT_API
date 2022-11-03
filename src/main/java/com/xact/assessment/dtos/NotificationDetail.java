package com.xact.assessment.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class NotificationDetail {
    String subject;
    List<String> to;
    List<String> cc;
    List<String> bcc;
    EmailHeader from;
    String replyTo;
    String contentType;
    String content;
}