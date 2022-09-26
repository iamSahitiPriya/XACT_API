/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.AssessmentStatus.Completed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {
    private UsersAssessmentsService usersAssessmentsService;
    private AssessmentService assessmentService;
    private AssessmentRepository assessmentRepository;
    private UsersAssessmentsRepository usersAssessmentsRepository;
    private AccessControlRepository accessControlRepository;

    private UserAssessmentModuleRepository userAssessmentModuleRepository;
    private ModuleRepository moduleRepository;

    @BeforeEach
    public void beforeEach() {
        usersAssessmentsService = mock(UsersAssessmentsService.class);
        assessmentRepository = mock(AssessmentRepository.class);
        usersAssessmentsRepository = mock(UsersAssessmentsRepository.class);
        accessControlRepository = mock(AccessControlRepository.class);
        assessmentService = new AssessmentService(usersAssessmentsService, assessmentRepository, usersAssessmentsRepository, accessControlRepository, userAssessmentModuleRepository, moduleRepository, assessmentMasterDataService);
    }

    @Test
    void shouldAddUsersToAssessment() {
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        assessmentRequest.setAssessmentName("assessment1");
        assessmentRequest.setTeamSize(1);
        assessmentRequest.setOrganisationName("org1");
        assessmentRequest.setIndustry("IT");
        assessmentRequest.setDomain("IT");
        List<UserDto> users = new ArrayList<>();
        UserDto user = new UserDto("test@gmail.com", UserRole.Owner);
        users.add(user);
        assessmentRequest.setUsers(users);

        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);


        Set<AssessmentUsers> assessmentUsers = new HashSet<>();
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(123);
        assessment.setAssessmentStatus(Active);
        assessment.setAssessmentName("assessment1");
        Organisation organisation = new Organisation();
        organisation.setOrganisationName("org1");
        organisation.setDomain("IT");
        organisation.setIndustry("IT");
        organisation.setSize(1);
        assessment.setOrganisation(organisation);


        UserId userId1 = new UserId("test@email.com", assessment);
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, AssessmentRole.Owner);
        assessmentUsers.add(assessmentUsers1);


        when(assessmentRepository.save(assessment)).thenReturn(assessment);
        doNothing().when(usersAssessmentsService).createUsersInAssessment(assessmentUsers);


        Assessment actualAssessment = assessmentService.createAssessment(assessmentRequest, loggedinUser);

        assertEquals(assessment.getAssessmentName(), actualAssessment.getAssessmentName());
        assertEquals(assessment.getAssessmentStatus(), actualAssessment.getAssessmentStatus());
    }

    @Test
    void getAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);
        Assessment mockAssessment = new Assessment();
        mockAssessment.setAssessmentId(assessmentId);
        AssessmentUsers assessmentUser = new AssessmentUsers();
        UserId userId = new UserId(loggedinUser.getUserEmail(), mockAssessment);
        assessmentUser.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(loggedinUser.getUserEmail(), assessmentId)).thenReturn(assessmentUser);

        Assessment assessment = assessmentService.getAssessment(assessmentId, loggedinUser);

        assertEquals(assessment, mockAssessment);
    }

    @Test
    void finishAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentName("assessmentName");
        assessment.setAssessmentStatus(Active);
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(assessmentId);
        expectedAssessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        when(assessmentRepository.update(assessment)).thenReturn(assessment);

        Assessment actualAssessment = assessmentService.finishAssessment(assessment);

        assertEquals(AssessmentStatus.Completed,actualAssessment.getAssessmentStatus());
    }

    @Test
    void reopenAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(assessmentId);
        expectedAssessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(Active);

        when(assessmentRepository.update(assessment)).thenReturn(assessment);

        Assessment actualAssessment = assessmentService.reopenAssessment(assessment);

        assertEquals( Active,actualAssessment.getAssessmentStatus());
    }

    @Test
    void shouldReturnAssessmentUsersListWithParticularAssessmentId() {
        List<AssessmentUsers> assessmentUsersList = new ArrayList<>();

        Integer assessmentId = 1;

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        UserId userId1 = new UserId("hello@gmail.com", assessment);
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, AssessmentRole.Facilitator);
        assessmentUsersList.add(assessmentUsers1);

        UserId userId2 = new UserId("new@gmail.com", assessment);
        AssessmentUsers assessmentUsers2 = new AssessmentUsers(userId2, AssessmentRole.Facilitator);
        assessmentUsersList.add(assessmentUsers2);

        List<String> expectedAssessmentUsersList = new ArrayList<>();
        for (AssessmentUsers eachUser : assessmentUsersList) {
            expectedAssessmentUsersList.add(eachUser.getUserId().getUserEmail());
        }
        when(usersAssessmentsRepository.findUserByAssessmentId(assessmentId, AssessmentRole.Facilitator)).thenReturn(assessmentUsersList);
        List<String> actualResponse = assessmentService.getAssessmentUsers(assessmentId);
        assertEquals(expectedAssessmentUsersList, actualResponse);

    }

    @Test
    void shouldUpdateAssessment() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setAssessmentStatus(Active);

        when(assessmentRepository.save(assessment)).thenReturn(assessment);

        assessment.setAssessmentName("New Assessment");

        when(assessmentRepository.update(assessment)).thenReturn(assessment);
        Set<AssessmentUsers> assessmentUsersSet = new HashSet<>(Set.of());
        UserId userId1 = new UserId("hello@gmail.com", assessment);
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, AssessmentRole.Facilitator);
        assessmentUsersSet.add(assessmentUsers1);

        UserId userId2 = new UserId("new@gmail.com", assessment);
        AssessmentUsers assessmentUsers2 = new AssessmentUsers(userId2, AssessmentRole.Facilitator);
        assessmentUsersSet.add(assessmentUsers2);

        doNothing().when(usersAssessmentsService).updateUsersInAssessment(assessmentUsersSet, assessment.getAssessmentId());

        assessmentService.updateAssessment(assessment, assessmentUsersSet);

        User user = new User();
        Profile profile = new Profile();
        profile.setEmail("hello@email.com");
        user.setProfile(profile);
        when(usersAssessmentsRepository.findByUserEmail(String.valueOf(user.getUserEmail()), 1)).thenReturn(assessmentUsers1);

        Assessment expectedAssessment = assessmentService.getAssessment(1, user);

        assertEquals(expectedAssessment.getAssessmentName(), assessment.getAssessmentName());

    }

    @Test
    void shouldGetTheTotalAssessmentCount() throws ParseException {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Date created2 = new Date(2022 - 6 - 1);
        Date updated2 = new Date(2022 - 6 - 11);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", organisation, Active, created1, updated1);
        Assessment assessment2 = new Assessment(2, "Name", organisation, AssessmentStatus.Completed, created2, updated2);

        List<Assessment> assessments=new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";

        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

        when(assessmentRepository.Total_Assessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate))).thenReturn(assessments);

        assertEquals(2,assessmentService.getTotalAssessments(startDate,endDate));

    }

    @Test
    void shouldGetTheTotalActiveAssessmentCount() throws ParseException {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Date created2 = new Date(2022 - 6 - 1);
        Date updated2 = new Date(2022 - 6 - 11);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "First_Name", organisation,Active, created2, updated2);
        Assessment assessment2 = new Assessment(2, "Second_Name", organisation,Active, created1, updated1);

        List<Assessment> assessments=new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";

        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

        when(assessmentRepository.Total_Active_Assessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate))).thenReturn(assessments);

        assertEquals(2,assessmentService.getTotalActiveAssessments(startDate,endDate));

    }

    @Test
    void shouldGetTheTotalCompletedAssessmentCount() throws ParseException {
         DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String createdAt1="2022-07-13";
        String updatedAt1="2022-09-24";
        String createdAt2="2022-06-01";
        String updatedAt2="2022-06-11";

        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment2 = new Assessment(1, "Name", organisation,Completed,  simpleDateFormat.parse(createdAt1), simpleDateFormat.parse(updatedAt1));
        Assessment assessment1 = new Assessment(2, "Name", organisation,Completed, simpleDateFormat.parse(createdAt2), simpleDateFormat.parse(updatedAt2));

        List<Assessment> assessments=new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";


        when(assessmentRepository.Total_Completed_Assessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate))).thenReturn(assessments);

        assertEquals(2,assessmentService.getTotalCompletedAssessments(startDate,endDate));

    }

    @Test
    void shouldGetTheAdminAssessmentData() throws ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String createdAt1="2022-07-13";
        String updatedAt1="2022-09-24";
        String createdAt2="2022-06-01";
        String updatedAt2="2022-06-11";

        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment2 = new Assessment(1, "Name", organisation,Completed,  simpleDateFormat.parse(createdAt1), simpleDateFormat.parse(updatedAt1));
        Assessment assessment1 = new Assessment(2, "Name", organisation, Active, simpleDateFormat.parse(createdAt2), simpleDateFormat.parse(updatedAt2));

        List<Assessment> assessments=new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";


        when(assessmentRepository.Total_Assessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate))).thenReturn(assessments);

        assertEquals(2,assessmentService.getAdminAssessmentsData(startDate,endDate).size());



    }


}
