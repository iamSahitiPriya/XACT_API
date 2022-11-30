package unit.com.xact.assessment.models;

import com.xact.assessment.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


class AssessmentTest {

    @Test
    void shouldCheckForAssessmentStatusWhenNotDeleted() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("testorg");
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setOrganisation(organisation);
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        boolean actualResponse = assessment.isEditable();
        Assertions.assertFalse(actualResponse);
    }

    @Test
    void shouldCheckForAssessmentStatusWhenDeleted() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("testorg");
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setDeleted(true);
        assessment.setOrganisation(organisation);
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        boolean actualResponse = assessment.isEditable();
        Assertions.assertFalse(actualResponse);
    }

    @Test
    void shouldCheckForAssessmentStatusWhenNotDeletedNotCompleted() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("testorg");
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(organisation);
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        boolean actualResponse = assessment.isEditable();
        Assertions.assertTrue(actualResponse);
    }

    @Test
    void shouldCheckForAssessmentState() {
        String userEmail = "dummytest@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        Organisation organisation = new Organisation();
        organisation.setSize(9);
        organisation.setIndustry("industry");
        organisation.setDomain("domain");
        organisation.setOrganisationName("organisation");
        assessment.setAssessmentName("assessmentName");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(organisation);
        AssessmentCategory category = new AssessmentCategory("name",true,"");
        category.setCategoryId(1);
        AssessmentModule module = new AssessmentModule("module",category,true,"");
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId(assessment,module);
        UserAssessmentModule userAssessmentModule = new UserAssessmentModule(assessmentModuleId,assessment,module);
        Set<UserAssessmentModule> assessmentModules= Collections.singleton(userAssessmentModule);
        assessment.setAssessmentModules(assessmentModules);
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);

        boolean actualCategoryStatus = assessment.getCategoryStatus();
        Assertions.assertTrue(actualCategoryStatus);
    }


    @Test
    void shouldGetOwnerTrue() {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("testorg");
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setOrganisation(organisation);
        Set<AssessmentUsers> assessmentUsers = new HashSet<>();

        AssessmentUsers assessmentUser1 = new AssessmentUsers();
        UserId userId1 = new UserId("some-facilitator@test.com", assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Facilitator);
        AssessmentUsers assessmentUser2 = new AssessmentUsers();
        UserId userId2 = new UserId(userEmail, assessment);
        assessmentUser2.setUserId(userId2);
        assessmentUser2.setRole(AssessmentRole.Owner);
        assessmentUsers.add(assessmentUser1);
        assessmentUsers.add(assessmentUser2);
        assessment.setAssessmentUsers(assessmentUsers);

        String actualResponse = assessment.getOwner();
        Assertions.assertEquals(userEmail,actualResponse);
    }


}
