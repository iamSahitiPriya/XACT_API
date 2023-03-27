package com.xact.assessment.dtos;

import lombok.Getter;

@Getter
public enum ContributorQuestionStatus {
    Rejected("Sent_For_Review"),Sent_For_Review("Draft"), Draft(""),Requested_For_Change("Sent_For_Review"),Published("Sent_For_Review");
    private final String initialState ;

    ContributorQuestionStatus(String initialState) {
        this.initialState = initialState;
    }

}

