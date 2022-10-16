package com.xact.assessment.services;

import com.xact.assessment.config.AppConfig;
import jakarta.inject.Singleton;

@Singleton
public class OrganisationService {
    private AppConfig appConfig;

    public OrganisationService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }
    public void getName(){
        System.out.println(appConfig.getUserName());
    }
}
