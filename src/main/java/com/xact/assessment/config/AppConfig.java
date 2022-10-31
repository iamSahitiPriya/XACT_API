package com.xact.assessment.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(value = "token", cliPrefix = "token")
public class AppConfig {
    private String username;
    private String password;
    private String grantType;
    private String scope;

}
