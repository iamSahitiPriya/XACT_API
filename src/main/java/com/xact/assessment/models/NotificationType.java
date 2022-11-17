package com.xact.assessment.models;

public enum NotificationType {
    Completed_V1("Notification_For_Complete_Assessment.vm","Assessment completed"),
    Created_V1("Notification_For_Create_Assessment.vm", "Assessment created"),
    AddUser_V1("Notification_For_Add_User.vm", "User added to assessment"),
    Reopened_V1("Notification_For_Reopen_Assessment.vm", "Assessment reopened");

    private final String templateResource;
    private final String emailSubject;

    NotificationType(String templateResource, String emailSubject) {
        this.templateResource = templateResource;
        this.emailSubject = emailSubject;
    }

    public  String getTemplateResource() {
        return this.templateResource;
    }

    public String getEmailSubject() {
        return this.emailSubject;
    }

}
