/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RatingDto{
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    RatingDto(int value) {
        this.value = value;
    }

    @JsonValue
    final int value() {
        return this.value;
    }
}
