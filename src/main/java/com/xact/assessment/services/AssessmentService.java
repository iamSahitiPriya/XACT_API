package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentsRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Singleton
public class AssessmentService {

    ModelMapper mapper = new ModelMapper();

    private final AssessmentsRepository assessmentsRepository;
    private final OrganisationService organisationService;
    private final UsersAssessmentsService usersAssessmentsService;

    public AssessmentService(AssessmentsRepository assessmentsRepository, OrganisationService organisationService, UsersAssessmentsService usersAssessmentsService) {
        this.assessmentsRepository = assessmentsRepository;
        this.organisationService = organisationService;
        this.usersAssessmentsService = usersAssessmentsService;
    }

    @Transactional
    public Assessment createAssessment(AssessmentRequest assessmentRequest, User user) {
        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.ACTIVE);

        Organisation organisation = mapper.map(assessmentRequest, Organisation.class);
        organisationService.createOrganisation(organisation);

        assessment.setOrganisation(organisation);
        createAssessment(assessment);

        List<AssessmentUsers> assessmentUsers = getAssessmentUsers(assessmentRequest, user, assessment);
        usersAssessmentsService.createUsersInAssessment(assessmentUsers);

        return assessment;
    }

    private List<AssessmentUsers> getAssessmentUsers(AssessmentRequest assessmentRequest, User loggedinUser, Assessment createdAssessment) {
        List<UserDto> users = assessmentRequest.getUsers();

        List<AssessmentUsers> assessmentUsers = new ArrayList<>();
        for (UserDto eachUser : users) {
            eachUser.setRole(UserRole.Facilitator);
            if (loggedinUser.getEmail().equals(eachUser.getEmail())) {
                eachUser.setRole(UserRole.Owner);
            }
            AssessmentUsers EachAssessmentUsers = mapper.map(eachUser, AssessmentUsers.class);
            EachAssessmentUsers.setUserId(new UserId(eachUser.getEmail(), createdAssessment));
            assessmentUsers.add(EachAssessmentUsers);

        }
        return assessmentUsers;
    }

    private Assessment createAssessment(Assessment assessment) {
        return assessmentsRepository.save(assessment);
    }

}
