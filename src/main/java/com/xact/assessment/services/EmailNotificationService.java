package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.EmailNotificationRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.*;

@Singleton
public class EmailNotificationService {
    private  final  TokenService tokenService;
    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailConfig emailConfig;
    private final EmailNotificationClient emailNotificationClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private static final String notificationMessage = "EMail sent successfully!";


    public EmailNotificationService(TokenService tokenService, EmailNotificationRepository emailNotificationRepository, EmailConfig emailConfig, EmailNotificationClient emailNotificationClient) {
        this.tokenService = tokenService;
        this.emailNotificationRepository = emailNotificationRepository;
        this.emailConfig = emailConfig;
        this.emailNotificationClient = emailNotificationClient;
    }

    @Scheduled(fixedDelay = "${email.delay}")
    public void sendEmailNotifications() throws JsonProcessingException {
            List<EmailNotifier> emailNotifierList = emailNotificationRepository.getNotificationDetailsToBeSend();

            if (!emailNotifierList.isEmpty()) {
                String accessToken = "Bearer " + tokenService.getToken(emailConfig.getScope());
                for (EmailNotifier emailNotifier : emailNotifierList) {
                    setEmailNotification(accessToken, emailNotifier);
                }
            }
    }

    private void setEmailNotification(String accessToken, EmailNotifier emailNotifier ) throws JsonProcessingException {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationDetail notificationDetail= new NotificationDetail();
        notificationDetail.setFrom(new EmailHeader());
        notificationDetail.setSubject(emailNotifier.getTemplateName().getEmailSubject());
        notificationDetail.setTo(Collections.singletonList(emailNotifier.getUserEmail()));
        notificationDetail.setBcc(new ArrayList<>());
        notificationDetail.setCc(new ArrayList<>());
        notificationDetail.setReplyTo("");
        notificationDetail.setContentType("text/html");
        String notificationContent = getNotificationContent(emailNotifier);

        notificationDetail.setContent(notificationContent);

        notificationRequest.setEmail(notificationDetail);
        String json = new ObjectMapper().writeValueAsString(notificationRequest);

        sendEmail(accessToken, emailNotifier, notificationDetail, json);

    }

    private void sendEmail(String accessToken, EmailNotifier emailNotifier, NotificationDetail notificationDetail, String json) {
            LOGGER.info("Sending notification to {}",notificationDetail.getTo());
            NotificationResponse notificationResponse = emailNotificationClient.sendNotification(accessToken, json);
            emailNotifier.setRetries(emailNotifier.getRetries() + 1);

            updateNotificationStatus(emailNotifier, notificationResponse);
    }

    private String getNotificationContent(EmailNotifier emailNotifier) throws JsonProcessingException {
        setVelocityProperty();
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        Template template = Velocity.getTemplate("templates/"+ emailNotifier.getTemplateName().getTemplateResource());
        EmailPayload emailPayload = new ObjectMapper().readValue(emailNotifier.getPayload(),EmailPayload.class);
        context.put("assessment", emailPayload);
        template.merge(context,writer);
        return writer.toString();
    }

    private void setVelocityProperty() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init( p );
    }

    private void updateNotificationStatus(EmailNotifier emailNotifier, NotificationResponse notificationResponse) {
        if(notificationResponse.getMessage().equals(notificationMessage)) {
            emailNotifier.setStatus(NotificationStatus.Y);
            emailNotificationRepository.update(emailNotifier);
        }
    }

    @SneakyThrows
    public void setNotification(Assessment assessment, String assessmentUserEmail, NotificationTemplateType notificationTemplateType ) {
        EmailNotifier emailNotifier = new EmailNotifier();
        emailNotifier.setUserEmail(assessmentUserEmail);
        emailNotifier.setTemplateName(notificationTemplateType);
        emailNotifier.setStatus(NotificationStatus.N);
        Map<String, String> payload = new HashMap<>();
        payload.put("assessment_id", String.valueOf(assessment.getAssessmentId()));
        payload.put("assessment_name", assessment.getAssessmentName());
        ObjectMapper objectMapper = new ObjectMapper();
        emailNotifier.setPayload(objectMapper.writeValueAsString(payload));
        saveNotification(emailNotifier);
    }

    public void setNotificationByRole(Assessment assessment, AssessmentUsers eachUser) {
        if(eachUser.getRole().equals(AssessmentRole.Owner))
            setNotification(assessment, eachUser.getUserId().getUserEmail(),NotificationTemplateType.Created);
        else if(eachUser.getRole().equals(AssessmentRole.Facilitator))
            setNotification(assessment, eachUser.getUserId().getUserEmail(),NotificationTemplateType.AddUser);
    }

    private void saveNotification(EmailNotifier emailNotifier) {
        emailNotificationRepository.save(emailNotifier);
    }

    public boolean isEmailMasked(){
        return emailConfig.isNotificationEnabled() && emailConfig.isMaskEmail();
    }
}

