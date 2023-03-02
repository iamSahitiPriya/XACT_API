package com.xact.assessment.models;

public enum NotificationType {
    COMPLETED_V1("v1_notification_for_complete_assessment.vm", "Assessment completed"),
    CREATED_V1("v1_notification_for_create_assessment.vm", "Assessment created"),
    ADD_USER_V1("v1_notification_for_add_user.vm", "You have been added to assessment"),
    REOPENED_V1("v1_notification_for_reopen_assessment.vm", "Assessment reopened"),
    DELETE_USER_V1("v1_notification_for_delete_user.vm","You have been removed from assessment"),
    DELETE_ASSESSMENT_V1("v1_notification_for_delete_assessment.vm","Assessment Deleted"),
    INACTIVE_V1("v1_notification_for_inactive_assessment.vm", "Action Required: Continue on your Assessment"),
    FEEDBACK_V1("v1_notification_for_feedback.vm", "Feedback for Thoughtworks X-Act");


    private final String templateResource;
    private final String emailSubject;

    NotificationType(String templateResource, String emailSubject) {
        this.templateResource = templateResource;
        this.emailSubject = emailSubject;
    }

    public String getTemplateResource() {
        return this.templateResource;
    }

    public String getEmailSubject() {
        return this.emailSubject;
    }

}
