/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.exceptions;

public class DuplicateRecordException extends  RuntimeException{

    public DuplicateRecordException(String message) {
        super(message);
    }
}
