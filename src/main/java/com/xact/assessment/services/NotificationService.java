package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.AssessmentAction;
import com.xact.assessment.dtos.EmailPayload;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.NotificationRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);


    public static final String ORGANISATION_NAME = "organisation_name";
    public static final String CREATED_AT = "created_at";
    public static final String ASSESSMENT_ID = "assessment_id";
    public static final String ASSESSMENT_NAME = "assessment_name";
    public static final String OWNER_NAME = "ownerName";
    public static final String OWNER_EMAIL = "ownerEmail";

    public static final String UPDATED_AT = "updated_at";
    public static final String COLLABORATORS = "collaborators";
    private final NotificationRepository notificationRepository;

    private final AssessmentService assessmentService;
    private final EmailConfig emailConfig;
    private final UserAuthService userAuthService;


    public NotificationService(NotificationRepository notificationRepository, AssessmentService assessmentService, EmailConfig emailConfig, UserAuthService userAuthService) {
        this.notificationRepository = notificationRepository;
        this.assessmentService = assessmentService;
        this.emailConfig = emailConfig;
        this.userAuthService = userAuthService;
    }

    @Scheduled(initialDelay = "${notification.feedback.initialDelay}", fixedDelay = "${notification.feedback.delay}")
    public void saveFeedbackNotificationForFinishedAssessments() {
        List<Assessment> assessments = assessmentService.getFinishedAssessments();
        List<Notification> notifications = notificationRepository.findByType(NotificationType.FEEDBACK_V1);
        assessments.forEach(assessment -> {
            try {
                Notification notification = getNotificationForFeedback(assessment);
                if (!isNotificationSent(assessment, notifications)) {
                    LOGGER.info("Save notifications for feedback ...");
                    saveNotification(notification);
                }
            } catch (JsonProcessingException e) {
                LOGGER.error("JsonProcessingException");
            }
        });
    }

    @SneakyThrows
    private boolean isNotificationSent(Assessment assessment, List<Notification> notifications) {
        for (Notification notification : notifications) {
            EmailPayload emailPayload = new ObjectMapper().readValue(notification.getPayload(), EmailPayload.class);
            if (emailPayload.getAssessmentId().equals(assessment.getAssessmentId().toString())) {
                return true;
            }
        }
        return false;
    }

    private Notification getNotificationForFeedback(Assessment assessment) throws JsonProcessingException {
        List<UserInfo> userInfos = getLoggedInUserInfo(assessment);
        Set<String> userEmails = userInfos.stream().map(UserInfo::getEmail).collect(Collectors.toSet());
        Notification notification = getNotification(userEmails);
        notification.setTemplateName(NotificationType.FEEDBACK_V1);
        Set<String> userDetails = userInfos.stream().map(UserInfo -> UserInfo.getFirstName() + " " + UserInfo.getLastName() + ":" + UserInfo.getEmail()).collect(Collectors.toSet());
        Map<String, String> payload = getPayloadForFeedback(assessment, userDetails);
        ObjectMapper objectMapper = new ObjectMapper();
        notification.setPayload(objectMapper.writeValueAsString(payload));

        return notification;
    }

    private Map<String, String> getPayloadForFeedback(Assessment assessment, Set<String> userNames) {
        Map<String, String> payload = getAssessmentCommonPayload(assessment);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a zz");
        String date = simpleDateFormat.format(assessment.getUpdatedAt());
        String time = timeFormat.format(assessment.getCreatedAt());
        String updatedAt = date + " " + time;
        payload.put(UPDATED_AT, updatedAt);
        payload.put(COLLABORATORS, String.join(",", userNames));

        return payload;
    }

    private List<UserInfo> getLoggedInUserInfo(Assessment assessment) {
        Set<String> assessmentUsers = assessment.getAssessmentUsers().stream().map(assessmentUser -> assessmentUser.getUserId().getUserEmail()).collect(Collectors.toSet());
        return userAuthService.getLoggedInUsers(assessmentUsers);
    }

    @SneakyThrows
    public Notification getNotification(Set<String> userEmails) {
        Notification notification = new Notification();
        notification.setUserEmail(String.join(",", userEmails));
        notification.setStatus(NotificationStatus.N);

        return notification;
    }

    public boolean isEmailMasked() {
        return emailConfig.isNotificationEnabled() && emailConfig.isMaskEmail();
    }

    public void update(Notification notification) {
        notificationRepository.update(notification);
    }

    @SneakyThrows
    public Notification setNotificationForCompleteAssessment(Assessment assessment) {
        Set<String> users = assessment.getAssessmentUsers().stream().map(assessmentUsers -> assessmentUsers.getUserId().getUserEmail()).collect(Collectors.toSet());
        Notification notification = getNotification(users);
        notification.setTemplateName(NotificationType.COMPLETED_V1);
        Map<String, String> payload = getAssessmentCommonPayload(assessment);
        ObjectMapper objectMapper = new ObjectMapper();
        notification.setPayload(objectMapper.writeValueAsString(payload));

        saveNotification(notification);
        return notification;
    }

    @SneakyThrows
    public Notification setNotificationForDeleteUser(Assessment assessment, Set<String> assessmentUsers) {
        if (!assessmentUsers.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Notification notification = getNotification(assessmentUsers);
            notification.setTemplateName(NotificationType.DELETE_USER_V1);
            Map<String, String> payload = getAssessmentCommonPayload(assessment);
            notification.setPayload(objectMapper.writeValueAsString(payload));

            saveNotification(notification);
            return notification;
        }
        return null;
    }

    @SneakyThrows
    public Notification setNotificationForAddUser(Assessment assessment, Set<String> assessmentUsers) {
        if (!assessmentUsers.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Notification notification = getNotification(assessmentUsers);
            notification.setTemplateName(NotificationType.ADD_USER_V1);
            Map<String, String> payload = getAssessmentCommonPayload(assessment);
            notification.setPayload(objectMapper.writeValueAsString(payload));

            saveNotification(notification);
            return notification;
        }
        return null;
    }

    @SneakyThrows
    public Notification setNotificationForReopenAssessment(Assessment assessment) {
        Set<String> users = assessment.getAssessmentUsers().stream().map(assessmentUsers -> assessmentUsers.getUserId().getUserEmail()).collect(Collectors.toSet());
        Notification notification = getNotification(users);
        notification.setTemplateName(NotificationType.REOPENED_V1);
        Map<String, String> payload = getAssessmentCommonPayload(assessment);
        ObjectMapper objectMapper = new ObjectMapper();
        notification.setPayload(objectMapper.writeValueAsString(payload));

        saveNotification(notification);
        return notification;
    }

    public Notification setNotificationForCreateAssessment(Assessment assessment) {
        Map<NotificationType, Set<String>> notificationsType = getNotificationTypeByUserRole(assessment.getAssessmentUsers(), AssessmentAction.CREATE);

        notificationsType.forEach((notificationType, users) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> payload;
            Notification notification = getNotification(users);
            notification.setTemplateName(notificationType);
            payload = getAssessmentCommonPayload(assessment);

            try {
                notification.setPayload(objectMapper.writeValueAsString(payload));
            } catch (JsonProcessingException e) {
                LOGGER.error("Error while parsing JSON");
            }

            saveNotification(notification);
        });
        return null;
    }

    private Map<String, String> getAssessmentCommonPayload(Assessment assessment) {
        Map<String, String> payload = new HashMap<>();
        payload.put(ASSESSMENT_ID, String.valueOf(assessment.getAssessmentId()));
        payload.put(ASSESSMENT_NAME, assessment.getAssessmentName());
        payload.put(ORGANISATION_NAME, assessment.getOrganisation().getOrganisationName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a zz");
        String date = simpleDateFormat.format(assessment.getCreatedAt());
        String time = timeFormat.format(assessment.getCreatedAt());
        String createdAt = date + " " + time;
        payload.put(CREATED_AT, createdAt);
        return payload;
    }

    private Map<String, String> getPayloadForDeleteAssessment(Assessment assessment) {
        Map<String, String> payload = getAssessmentCommonPayload(assessment);
        UserInfo userInfo = userAuthService.getUserInfo(getAssessmentOwner(assessment));
        payload.put(OWNER_NAME, userInfo.getFullName());
        payload.put(OWNER_EMAIL, getAssessmentOwner(assessment));
        return payload;
    }

    private String getAssessmentOwner(Assessment assessment) {
        Optional<AssessmentUser> owner = assessment.getOwner();
        return owner.get().getUserId().getUserEmail();
    }

    public Notification setNotificationForDeleteAssessment(Assessment assessment) {
        Set<String> users = assessment.getAssessmentUsers().stream().map(user -> user.getUserId().getUserEmail()).collect(Collectors.toSet());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> payload;
        Notification notification = getNotification(users);
        notification.setTemplateName(NotificationType.DELETE_ASSESSMENT_V1);
        payload = getPayloadForDeleteAssessment(assessment);

        try {
            notification.setPayload(objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing JSON");
        }
        saveNotification(notification);
        return notification;
    }

    private Map<NotificationType, Set<String>> getNotificationTypeByUserRole(Set<AssessmentUser> assessmentUsers, AssessmentAction action) {
        Map<NotificationType, Set<String>> notifications = new EnumMap<>(NotificationType.class);
        Set<String> facilitatorEmails = new HashSet<>();
        for (AssessmentUser eachUser : assessmentUsers) {

            if (action.equals(AssessmentAction.DELETE))
                notifications.put(NotificationType.DELETE_ASSESSMENT_V1, facilitatorEmails);

            if (eachUser.getRole().equals(AssessmentRole.Owner)) {
                setNotificationTypeForOwner(notifications, eachUser, action);
            } else if (eachUser.getRole().equals(AssessmentRole.Facilitator)) {
                facilitatorEmails.add(eachUser.getUserId().getUserEmail());
            }
        }
        if (!facilitatorEmails.isEmpty())
            setNotificationTypeForFacilitator(notifications, facilitatorEmails, action);

        return notifications;
    }

    private void setNotificationTypeForFacilitator(Map<NotificationType, Set<String>> notifications, Set<String> facilitatorEmails, AssessmentAction action) {
        if (action.equals(AssessmentAction.CREATE))
            notifications.put(NotificationType.ADD_USER_V1, facilitatorEmails);
        if (action.equals(AssessmentAction.DELETE))
            notifications.put(NotificationType.DELETE_ASSESSMENT_V1, facilitatorEmails);
    }

    private void setNotificationTypeForOwner(Map<NotificationType, Set<String>> notifications, AssessmentUser assessmentUser, AssessmentAction action) {
        if (action.equals(AssessmentAction.CREATE))
            notifications.put(NotificationType.CREATED_V1, Collections.singleton(assessmentUser.getUserId().getUserEmail()));
        if (action.equals(AssessmentAction.DELETE))
            notifications.put(NotificationType.DELETE_ASSESSMENT_V1, Collections.singleton(assessmentUser.getUserId().getUserEmail()));
    }

    private void saveNotification(Notification notification) {
        LOGGER.info("Saving notification for {}", notification.getTemplateName());
        try {
            notificationRepository.save(notification);
        } catch (Exception exception) {
            LOGGER.error("Notification not saved");
        }
    }


    public List<Notification> getTop50ByStatusAndRetriesLessThan(Integer maximumRetries) {
        return notificationRepository.findTop50ByStatusAndRetriesLessThan(NotificationStatus.N, maximumRetries);
    }

}

