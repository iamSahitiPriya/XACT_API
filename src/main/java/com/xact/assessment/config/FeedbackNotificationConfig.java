/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(value = "notification.feedback", cliPrefix = "notification.feedback")
public class FeedbackNotificationConfig {
    private Integer durationInDays;

}
