/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

public enum ContributorRole {
    AUTHOR, REVIEWER;


    public boolean isStatusValid(ContributorQuestionStatus status) {
        return isStatusValidForAuthor(status) || isStatusValidForReviewer(status);
    }

    private boolean isStatusValidForReviewer(ContributorQuestionStatus status) {
        return this.equals(REVIEWER) && (status == ContributorQuestionStatus.REQUESTED_FOR_CHANGE || status == ContributorQuestionStatus.PUBLISHED || status == ContributorQuestionStatus.REJECTED);
    }

    private boolean isStatusValidForAuthor(ContributorQuestionStatus status) {
        return this.equals(AUTHOR) && status == ContributorQuestionStatus.SENT_FOR_REVIEW;
    }
}
