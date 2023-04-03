package com.xact.assessment.dtos;

public enum ContributorRole {
    Author("Author"), Reviewer("Reviewer");

    private final String role;

    ContributorRole(String role) {
        this.role = role;
    }

    public boolean isStatusValid(ContributorQuestionStatus status) {
        return (this.role.equals(Author.role) && status == ContributorQuestionStatus.SENT_FOR_REVIEW) || (this.role.equals(Reviewer.role) && (status == ContributorQuestionStatus.REQUESTED_FOR_CHANGE || status == ContributorQuestionStatus.PUBLISHED || status == ContributorQuestionStatus.REJECTED));
    }
}
