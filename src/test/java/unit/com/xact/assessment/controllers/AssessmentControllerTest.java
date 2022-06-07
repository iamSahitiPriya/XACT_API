///*
// *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
// */
//
//package unit.com.xact.assessment.controllers;
//
//import com.xact.assessment.controllers.AssessmentController;
//import com.xact.assessment.dtos.*;
//import com.xact.assessment.models.*;
//import com.xact.assessment.services.AssessmentService;
//import com.xact.assessment.services.UserAuthService;
//import com.xact.assessment.services.UsersAssessmentsService;
//import io.micronaut.http.HttpResponse;
//import io.micronaut.security.authentication.Authentication;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class AssessmentControllerTest {
//
//
//    private final Authentication authentication = Mockito.mock(Authentication.class);
//    private final UsersAssessmentsService usersAssessmentsService = Mockito.mock(UsersAssessmentsService.class);
//    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
//    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
//    private final AssessmentController assessmentController = new AssessmentController(usersAssessmentsService, userAuthService, assessmentService);
//
//    @Test
//    void testGetAssessments() {
//
//        Date created = new Date(2022 - 4 - 13);
//        Date updated = new Date(2022 - 4 - 13);
//        User user = new User();
//        String userEmail = "hello@thoughtworks.com";
//        Profile profile = new Profile();
//        profile.setEmail(userEmail);
//        user.setProfile(profile);
//        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
//        Assessment assessment = new Assessment(1, "xact", organisation, AssessmentStatus.Active, created, updated);
//        Map<String, Object> authMap = new HashMap<>();
//        authMap.put("sub", userEmail);
//        when(usersAssessmentsService.findAssessments(userEmail)).thenReturn(Collections.singletonList(assessment));
//        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
//        AssessmentResponse expectedAssessment = new AssessmentResponse();
//        expectedAssessment.setAssessmentId(1);
//        expectedAssessment.setAssessmentName("xact");
//        expectedAssessment.setOrganisationName("abc");
//        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.Active);
//        expectedAssessment.setUpdatedAt(updated);
//        HttpResponse<List<AssessmentResponse>> actualAssessments = assessmentController.getAssessments(authentication);
//
//        assertEquals(expectedAssessment.getAssessmentId(), actualAssessments.body().get(0).getAssessmentId());
//        assertEquals(expectedAssessment.getAssessmentName(), actualAssessments.body().get(0).getAssessmentName());
//        assertEquals(expectedAssessment.getAssessmentStatus(), actualAssessments.body().get(0).getAssessmentStatus());
//        assertEquals(expectedAssessment.getOrganisationName(), actualAssessments.body().get(0).getOrganisationName());
//        assertEquals(expectedAssessment.getUpdatedAt(), actualAssessments.body().get(0).getUpdatedAt());
//        verify(usersAssessmentsService).findAssessments(userEmail);
//    }
//
//    @Test
//    void testCreateAssessment() {
//        AssessmentRequest assessmentRequest = new AssessmentRequest();
//        assessmentRequest.setAssessmentName("Dummy assessment");
//        assessmentRequest.setDomain("Dummy domain");
//        assessmentRequest.setIndustry("Dummy Industry");
//        assessmentRequest.setOrganisationName("Dummy Org");
//        assessmentRequest.setTeamSize(10);
//        UserDto userDto = new UserDto();
//        userDto.setEmail("dummy@thoughtworks.com");
//        userDto.setRole(UserRole.Owner);
//        List<UserDto> users = Collections.singletonList(userDto);
//        assessmentRequest.setUsers(users);
//        User user = new User();
//        String userEmail = "hello@thoughtworks.com";
//        Profile profile = new Profile();
//        profile.setEmail(userEmail);
//        user.setProfile(profile);
//        Date created = new Date(2022 - 4 - 13);
//        Date updated = new Date(2022 - 4 - 13);
//        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
//        Assessment assessment = new Assessment(1, "xact", organisation, AssessmentStatus.Active, created, updated);
//
//
//        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
//        when(assessmentService.createAssessment(assessmentRequest, user)).thenReturn(assessment);
//
//        HttpResponse<AssessmentResponse> actualAssessments = assessmentController.createAssessment(assessmentRequest, authentication);
//
//        System.out.println(actualAssessments.body().getOrganisationName());
//        assertNotNull(actualAssessments.body().getAssessmentId());
//        assertEquals(assessment.getAssessmentName(), actualAssessments.body().getAssessmentName());
//        assertEquals(assessment.getAssessmentStatus().name(), actualAssessments.body().getAssessmentStatus().name());
//        assertEquals(assessment.getOrganisation().getOrganisationName(), actualAssessments.body().getOrganisationName());
//    }
//
//    @Test
//    void testGetAssessment() {
//
//        Date created = new Date(2022 - 4 - 13);
//        Date updated = new Date(2022 - 4 - 13);
//        User user = new User();
//        String userEmail = "hello@thoughtworks.com";
//        Profile profile = new Profile();
//        profile.setEmail(userEmail);
//        user.setProfile(profile);
//        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
//        Assessment assessment = new Assessment(123, "xact", organisation, AssessmentStatus.Active, created, updated);
//        Map<String, Object> authMap = new HashMap<>();
//        authMap.put("sub", userEmail);
//        Integer assessmentId=123;
//        when(assessmentService.getAssessment(assessmentId,user)).thenReturn(assessment);
//        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
//        AssessmentResponse expectedAssessment = new AssessmentResponse();
//        expectedAssessment.setAssessmentId(123);
//        expectedAssessment.setAssessmentName("xact");
//        expectedAssessment.setOrganisationName("abc");
//        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.Active);
//        expectedAssessment.setUpdatedAt(updated);
//        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.getAssessment(assessmentId,authentication);
//
//        assertEquals(expectedAssessment.getAssessmentId(), actualAssessment.body().getAssessmentId());
//        assertEquals(expectedAssessment.getAssessmentName(), actualAssessment.body().getAssessmentName());
//        assertEquals(expectedAssessment.getAssessmentStatus(), actualAssessment.body().getAssessmentStatus());
//        assertEquals(expectedAssessment.getOrganisationName(), actualAssessment.body().getOrganisationName());
//        assertEquals(expectedAssessment.getUpdatedAt(), actualAssessment.body().getUpdatedAt());
//        verify(assessmentService).getAssessment(assessmentId,user);
//    }
//
//    @Test
//    void testFinishAssessment() {
//
//        Date created = new Date(2022 - 4 - 13);
//        Date updated = new Date(2022 - 4 - 13);
//        User user = new User();
//        String userEmail = "hello@thoughtworks.com";
//        Profile profile = new Profile();
//        profile.setEmail(userEmail);
//        user.setProfile(profile);
//        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
//        Assessment assessment = new Assessment(123, "xact", organisation, AssessmentStatus.Active, created, updated);
//        Map<String, Object> authMap = new HashMap<>();
//        authMap.put("sub", userEmail);
//        Integer assessmentId=123;
//        Assessment expectedAssessment = new Assessment();
//        expectedAssessment.setAssessmentId(123);
//        expectedAssessment.setAssessmentName("xact");
//        expectedAssessment.setAssessmentStatus(AssessmentStatus.Completed);
//        expectedAssessment.setUpdatedAt(updated);
//        when(assessmentService.getAssessment(assessmentId,user)).thenReturn(assessment);
//        when(assessmentService.finishAssessment(assessment)).thenReturn(expectedAssessment);
//        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
//
//        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.finishAssessment(assessmentId,authentication);
//
//        assertEquals(expectedAssessment.getAssessmentId(), actualAssessment.body().getAssessmentId());
//        assertEquals(expectedAssessment.getAssessmentName(), actualAssessment.body().getAssessmentName());
//        assertEquals(expectedAssessment.getAssessmentStatus().name(), actualAssessment.body().getAssessmentStatus().name());
//        verify(assessmentService).finishAssessment(assessment);
//    }
//
//    @Test
//    void testReopenAssessment() {
//
//        Date created = new Date(2022 - 4 - 13);
//        Date updated = new Date(2022 - 4 - 13);
//        User user = new User();
//        String userEmail = "hello@thoughtworks.com";
//        Profile profile = new Profile();
//        profile.setEmail(userEmail);
//        user.setProfile(profile);
//        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
//        Assessment assessment = new Assessment(123, "xact", organisation, AssessmentStatus.Completed, created, updated);
//        Map<String, Object> authMap = new HashMap<>();
//        authMap.put("sub", userEmail);
//        Integer assessmentId=123;
//        Assessment expectedAssessment = new Assessment();
//        expectedAssessment.setAssessmentId(123);
//        expectedAssessment.setAssessmentName("xact");
//        expectedAssessment.setAssessmentStatus(AssessmentStatus.Active);
//        expectedAssessment.setUpdatedAt(updated);
//        when(assessmentService.getAssessment(assessmentId,user)).thenReturn(assessment);
//        when(assessmentService.reopenAssessment(assessment)).thenReturn(expectedAssessment);
//        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
//
//        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.reopenAssessment(assessmentId,authentication);
//
//        assertEquals(expectedAssessment.getAssessmentId(), actualAssessment.body().getAssessmentId());
//        assertEquals(expectedAssessment.getAssessmentName(), actualAssessment.body().getAssessmentName());
//        assertEquals(expectedAssessment.getAssessmentStatus().name(), actualAssessment.body().getAssessmentStatus().name());
//        verify(assessmentService).reopenAssessment(assessment);
//    }
//
//
//
//}
