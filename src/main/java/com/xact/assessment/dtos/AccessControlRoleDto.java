/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.xact.assessment.models.AccessControlRoles;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Introspected
@Setter
@NoArgsConstructor
public class AccessControlRoleDto {
    private String email;
    private AccessControlRoles accessControlRoles;
}
