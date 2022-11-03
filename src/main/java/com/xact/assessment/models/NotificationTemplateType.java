package com.xact.assessment.models;

public enum NotificationTemplateType {
    Completed("Notification_For_Complete_Assessment.vm"),
    Created("Notification_For_Create_Assessment.vm"),
    AddUser("Notification_For_Add_User.vm"),
    Reopened("Notification_For_Reopen_Assessment.vm");

    private final String templateResource;

    NotificationTemplateType(String templateResource) {
        this.templateResource = templateResource;
    }

    public  String getTemplateResource() {
        return this.templateResource;
    }

}
