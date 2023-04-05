/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

public enum ContributorRole {
    AUTHOR("Author"), REVIEWER("Reviewer");

    private final String role;

    ContributorRole(String role) {
        this.role = role;
    }

    public boolean isStatusValid(ContributorQuestionStatus status) {
        return (this.role.equals(AUTHOR.role) && status == ContributorQuestionStatus.SENT_FOR_REVIEW) || (this.role.equals(REVIEWER.role) && (status == ContributorQuestionStatus.REQUESTED_FOR_CHANGE || status == ContributorQuestionStatus.PUBLISHED || status == ContributorQuestionStatus.REJECTED));
    }
}
