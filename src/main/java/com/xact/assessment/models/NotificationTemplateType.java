package com.xact.assessment.models;

public enum NotificationTemplateType {
    Completed("Notification_For_Complete_Assessment.vm","Complete Assessment"),
    Created("Notification_For_Create_Assessment.vm", "Assessment Created"),
    AddUser("Notification_For_Add_User.vm", "User added"),
    Reopened("Notification_For_Reopen_Assessment.vm", "Assessment Reopened");

    private final String templateResource;
    private final String emailSubject;

    NotificationTemplateType(String templateResource, String emailSubject) {
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
