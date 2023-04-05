/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.Getter;

@Getter
public enum ContributorQuestionStatus {
    REJECTED, SENT_FOR_REVIEW, DRAFT, REQUESTED_FOR_CHANGE, PUBLISHED;

}

