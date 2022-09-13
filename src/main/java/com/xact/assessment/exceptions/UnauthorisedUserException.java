/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.exceptions;

public class UnauthorisedUserException extends RuntimeException{
    public UnauthorisedUserException(String message) {
        super(message);
    }
}
