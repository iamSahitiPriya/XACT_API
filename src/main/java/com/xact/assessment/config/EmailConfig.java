package com.xact.assessment.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(value = "email", cliPrefix = "email")
public class EmailConfig {
    private String scope;
    private boolean notificationEnabled;
    private boolean maskEmail;
    private String defaultEmail;
    private String fromEmail;
    private String name;
}
