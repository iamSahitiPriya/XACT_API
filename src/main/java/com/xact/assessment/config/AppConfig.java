package com.xact.assessment.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(value = "application", cliPrefix = "application")
public class AppConfig {
    private String userName;
    private String userPassword;
}
