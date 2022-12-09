/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.exceptions;

public class InvalidEmailException extends  RuntimeException{

    public InvalidEmailException(String message) {
        super(message);
    }
}
