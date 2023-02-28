/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.NotificationRepository;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.NotificationService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.data.exceptions.EmptyResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    NotificationService notificationService;
    NotificationRepository notificationRepository = mock(NotificationRepository.class);
    EmailConfig emailConfig = mock(EmailConfig.class);
    UserAuthService userAuthService = mock(UserAuthService.class);


    public NotificationServiceTest() {
        notificationService = new NotificationService(notificationRepository, emailConfig, userAuthService);
    }

    @Test
    void shouldSetNotificationWhenAssessmentIsCreated() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);

        Notification notification = new Notification(1, NotificationType.ADD_USER_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());
        when(notificationRepository.save(notification)).thenReturn(notification);


        notificationService.setNotificationForCreateAssessment(assessment);
        notificationRepository.save(notification);

        verify(notificationRepository).save(notification);
    }


    @Test
    void shouldReturnTrueWhenNotificationEnabledAndMaskEnabled() {
        when(emailConfig.isMaskEmail()).thenReturn(true);
        when(emailConfig.isNotificationEnabled()).thenReturn(true);

        Assertions.assertTrue(notificationService.isEmailMasked());
    }

    @Test
    void shouldUpdateNotificationAfterSendingNotification() {
        Notification notification = new Notification(1, NotificationType.CREATED_V1, "brindha.e@thoughtworks.com", "{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\"}", NotificationStatus.N, 0, new Date(), new Date());
        Notification notification1 = new Notification(1, NotificationType.CREATED_V1, "brindha.e@thoughtworks.com", "{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\"}", NotificationStatus.Y, 0, new Date(), new Date());

        when(notificationRepository.update(notification)).thenReturn(notification1);

        notificationService.update(notification);

        Assertions.assertEquals(NotificationStatus.Y, notification1.getStatus());
    }

    @Test
    void shouldSetNotificationWhenAnUserIsAdded() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        Set<String> users = new HashSet<>();
        users.add(assessmentUser1.getUserId().getUserEmail());

        Notification notification = new Notification(1, NotificationType.ADD_USER_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());
        when(notificationRepository.save(notification)).thenReturn(notification);


        notificationService.setNotificationForAddUser(assessment, users);
        notificationRepository.save(notification);

        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldSetNotificationWhenAnUserIsDeleted() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        Set<String> users = new HashSet<>();
        users.add(assessmentUser1.getUserId().getUserEmail());

        Notification notification = new Notification(1, NotificationType.DELETE_USER_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());


        notificationService.setNotificationForDeleteUser(assessment, users);
        notificationRepository.save(notification);


        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldNotSetNotificationWhenUpdatedUsersAreNull() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        Set<String> users = new HashSet<>();

        Notification notification = new Notification(1, NotificationType.DELETE_USER_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());
        Notification notification1 = new Notification(1, NotificationType.ADD_USER_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationRepository.save(notification1)).thenReturn(notification1);

        notificationService.setNotificationForAddUser(assessment, users);
        notificationRepository.save(notification1);
        notificationService.setNotificationForDeleteUser(assessment, users);
        notificationRepository.save(notification);


        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldSetNotificationWhenAssessmentIsDeleted() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        UserInfo userInfo = new UserInfo(email, "firstName", "lastName", "enUS");

        Notification notification = new Notification(1, NotificationType.DELETE_ASSESSMENT_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(userAuthService.getUserInfo(email)).thenReturn(userInfo);



        notificationService.setNotificationForDeleteAssessment(assessment);
        notificationRepository.save(notification);

        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldGetTop50ByStatusAndRetriesLessThan() throws JsonProcessingException {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "IT Consultant", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("hello");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -5);
        Date updatedDate = calendar.getTime();

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        Set<String> users = new HashSet<>();
        users.add(assessmentUser1.getUserId().getUserEmail());

        Notification notification = new Notification(1, NotificationType.INACTIVE_V1, email, "{\"assessment_name\":\"hello\",\"created_at\":\"17-Feb-2023 12:26 pm IST\",\"assessment_id\":\"1\",\"organisation_name\":\"IT Consultant\"}", NotificationStatus.N, 0, new Date(), updatedDate);

        when(notificationRepository.findTop50ByStatusAndRetriesLessThan(NotificationStatus.N, 2)).thenReturn(Collections.singletonList(notification));

        notificationService.getTop50ByStatusAndRetriesLessThan(2);

        verify(notificationRepository).findTop50ByStatusAndRetriesLessThan(NotificationStatus.N, 2);

    }

    @Test
    void shouldSetNotificationForInactiveAssessment() throws JsonProcessingException {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "IT Consultant", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("hello");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -5);
        Date updatedDate = calendar.getTime();

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        Set<String> users = new HashSet<>();
        users.add(assessmentUser1.getUserId().getUserEmail());

        Notification notification = new Notification(1, NotificationType.INACTIVE_V1, email, "{\"assessment_name\":\"hello\",\"created_at\":\"17-Feb-2023 12:26 pm IST\",\"assessment_id\":\"1\",\"organisation_name\":\"IT Consultant\"}", NotificationStatus.N, 0, new Date(), updatedDate);

        doNothing().when(notificationRepository).delete(notification);
        when(notificationRepository.save(notification)).thenReturn(notification);

        notificationService.setNotificationForInactiveAssessment(assessment, Collections.singletonList(notification));
        notificationRepository.save(notification);

        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldResendNotificationToInactiveAssessments() throws JsonProcessingException {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "IT Consultant", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("hello");
        assessment.setCreatedAt(new Date(2023, Calendar.FEBRUARY, 17));
        assessment.setOrganisation(organisation);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -5);
        Date updatedDate = calendar.getTime();

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        Set<String> users = new HashSet<>();
        users.add(assessmentUser1.getUserId().getUserEmail());

        Notification notification = new Notification(1, NotificationType.INACTIVE_V1, email, "{\"assessment_name\":\"hello\",\"created_at\":\"17-Feb-3923 12:00 am IST\",\"assessment_id\":\"1\",\"organisation_name\":\"IT Consultant\"}", NotificationStatus.N, 0, new Date(), updatedDate);
        System.out.println(assessment.getCreatedAt());
        doNothing().when(notificationRepository).delete(notification);
        when(notificationRepository.save(notification)).thenReturn(notification);

        notificationService.setNotificationForInactiveAssessment(assessment, Collections.singletonList(notification));
        notificationRepository.save(notification);

        verify(notificationRepository).save(notification);
    }

    @Test
    void shouldHandleEmptyResultException() {

        when(notificationRepository.findByTemplateName(any(NotificationType.class))).thenThrow(new EmptyResultException());
        List<Notification> actualNotification = notificationService.getNotificationBy(NotificationType.INACTIVE_V1);

        Assertions.assertEquals(0, actualNotification.size());

    }
    @Test
    void shouldSaveNotificationForFeedback() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setUpdatedAt(new Date(22 - 11 - 2022));
        assessment.setOrganisation(organisation);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        String payload = "{\"assessment_name\":\"New Assessment\",\"created_at\":\"27-Jan-2023 06:33 pm IST\",\"assessment_id\":\"2\",\"organisation_name\":\"EQ International\"}";
        Notification notification = new Notification(1, NotificationType.FEEDBACK_V1, email, payload, NotificationStatus.N, 0, new Date(22 - 10 - 2022), new Date(22 - 10 - 2022));

        when(assessmentService.getFinishedAssessments()).thenReturn(Collections.singletonList(assessment));
        when(notificationRepository.findByType(NotificationType.FEEDBACK_V1)).thenReturn(Collections.singletonList(notification));

        notificationService.saveFeedbackNotificationForFinishedAssessments(assessment, Collections.singletonList(notification));

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void shouldNotSaveNotificationForFeedbackWhenEmailHasAlreadySent() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setUpdatedAt(new Date(22 - 11 - 2022));
        assessment.setOrganisation(organisation);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        String payload = "{\"assessment_name\":\"New Assessment\",\"created_at\":\"27-Jan-2023 06:33 pm IST\",\"assessment_id\":\"1\",\"organisation_name\":\"EQ International\"}";

        Notification notification = new Notification(1, NotificationType.FEEDBACK_V1, email, payload, NotificationStatus.N, 0, new Date(22 - 10 - 2022), new Date(22 - 10 - 2022));

        when(assessmentService.getFinishedAssessments()).thenReturn(Collections.singletonList(assessment));
        when(notificationRepository.findByType(NotificationType.FEEDBACK_V1)).thenReturn(Collections.singletonList(notification));

        notificationService.saveFeedbackNotificationForFinishedAssessments(assessment, Collections.singletonList(notification));
        notificationService.findByType(NotificationType.FEEDBACK_V1);

        verify(notificationRepository, Mockito.times(0)).save(any(Notification.class));

        verify(notificationRepository).findByType( NotificationType.FEEDBACK_V1);
    }


}
