package com.xact.assessment.dtos;

public enum ContributorRole {
    Author("Author"), Reviewer("Reviewer");

    private final String role;

    ContributorRole(String role) {
        this.role = role;
    }

    public boolean isStatusValid(ContributorQuestionStatus status) {
        return (this.role.equals(Author.role) && status == ContributorQuestionStatus.Sent_For_Review) || (this.role.equals(Reviewer.role) && (status == ContributorQuestionStatus.Requested_For_Change || status == ContributorQuestionStatus.Published || status == ContributorQuestionStatus.Rejected));
    }
}
