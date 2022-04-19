package com.xact.assessment.controllers;

import com.xact.assessment.dtos.AssessmentResponseDto;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.services.UsersAssessmentsService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static com.xact.assessment.constants.AuthConstants.EMAIL;

@Controller("/v1/assessments")
public class AssessmentController {
    private UsersAssessmentsService usersAssessmentsService;
    private ModelMapper modelMapper = new ModelMapper();

    public AssessmentController(UsersAssessmentsService usersAssessmentsService) {
        this.usersAssessmentsService = usersAssessmentsService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public List<AssessmentResponseDto> getAssessments(Authentication authentication) {
        String userEmail = (String) authentication.getAttributes().get(EMAIL);

        List<Assessment> assessments = usersAssessmentsService.findAssessments(userEmail);
        List<AssessmentResponseDto> assessmentResponseDtos = new ArrayList<>();
        assessments.forEach(assessment -> assessmentResponseDtos.add(modelMapper.map(assessment, AssessmentResponseDto.class)));
        return assessmentResponseDtos;
    }


}
