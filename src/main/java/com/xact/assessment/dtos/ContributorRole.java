package com.xact.assessment.dtos;

public enum ContributorRole {
    Author,Reviewer;

    public boolean isStatusValidForReviewer(ContributorQuestionStatus status) {
        return (status == ContributorQuestionStatus.Requested_For_Change || status == ContributorQuestionStatus.Published || status == ContributorQuestionStatus.Rejected);
    }
}
