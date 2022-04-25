package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class AssessmentService {

    ModelMapper mapper = new ModelMapper();

    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentRepository assessmentRepository;

    public AssessmentService(UsersAssessmentsService usersAssessmentsService, AssessmentRepository assessmentRepository) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
    }

    @Transactional
    public Assessment createAssessment(AssessmentRequest assessmentRequest, User user) {
        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        Organisation organisation = mapper.map(assessmentRequest, Organisation.class);
        assessment.setOrganisation(organisation);
        List<AssessmentUsers> assessmentUsers = getAssessmentUsers(assessmentRequest, user, assessment);

        createAssessment(assessment);
        usersAssessmentsService.createUsersInAssessment(assessmentUsers);

        return assessment;
    }

    private void createAssessment(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    private List<AssessmentUsers> getAssessmentUsers(AssessmentRequest assessmentRequest, User loggedinUser, Assessment assessment) {
        List<UserDto> users = assessmentRequest.getUsers();

        List<AssessmentUsers> assessmentUsers = new ArrayList<>();
        for (UserDto eachUser : users) {
            eachUser.setRole(UserRole.Facilitator);
            if (loggedinUser.getEmail().equals(eachUser.getEmail())) {
                eachUser.setRole(UserRole.Owner);
            }
            AssessmentUsers assessmentUser = mapper.map(eachUser, AssessmentUsers.class);
            assessmentUser.setUserId(new UserId(eachUser.getEmail(), assessment));
            assessmentUsers.add(assessmentUser);

        }
        return assessmentUsers;
    }

}
