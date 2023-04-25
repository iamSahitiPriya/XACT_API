/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.exceptions;

public class InvalidContributorException extends RuntimeException {
    public InvalidContributorException(String message){
        super(message);
    }
}
