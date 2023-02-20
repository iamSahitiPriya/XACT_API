package com.xact.assessment.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(value = "profile", cliPrefix = "profile")
public class ProfileConfig {
    private String type;
    private String url;
    private String homePageUrl;
    private String microSiteUrl;
    private String supportUrl;
    private String feedbackUrl;
}
