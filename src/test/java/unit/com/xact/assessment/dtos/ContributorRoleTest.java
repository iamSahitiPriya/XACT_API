package unit.com.xact.assessment.dtos;

import com.xact.assessment.dtos.ContributorQuestionStatus;
import com.xact.assessment.dtos.ContributorRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContributorRoleTest {

    @Test
    void isStatusValidForAuthorValid() {
        assertTrue(ContributorRole.AUTHOR.isStatusValid(ContributorQuestionStatus.SENT_FOR_REVIEW));
    }

    @Test
    void isStatusValidForAuthorInvalid() {
        assertFalse(ContributorRole.AUTHOR.isStatusValid(ContributorQuestionStatus.DRAFT));
        assertFalse(ContributorRole.AUTHOR.isStatusValid(ContributorQuestionStatus.REQUESTED_FOR_CHANGE));
        assertFalse(ContributorRole.AUTHOR.isStatusValid(ContributorQuestionStatus.PUBLISHED));
        assertFalse(ContributorRole.AUTHOR.isStatusValid(ContributorQuestionStatus.REJECTED));

    }

    @Test
    void isStatusValidForReviewerValid() {
        assertTrue(ContributorRole.REVIEWER.isStatusValid(ContributorQuestionStatus.REQUESTED_FOR_CHANGE));
        assertTrue(ContributorRole.REVIEWER.isStatusValid(ContributorQuestionStatus.PUBLISHED));
        assertTrue(ContributorRole.REVIEWER.isStatusValid(ContributorQuestionStatus.REJECTED));
    }

    @Test
    void isStatusValidForReviewerInvalid() {
        assertFalse(ContributorRole.REVIEWER.isStatusValid(ContributorQuestionStatus.DRAFT));
        assertFalse(ContributorRole.REVIEWER.isStatusValid(ContributorQuestionStatus.SENT_FOR_REVIEW));
    }
}