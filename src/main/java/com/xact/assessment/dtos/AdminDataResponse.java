/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
@AllArgsConstructor
public class AdminDataResponse {
    CategoryDto category;
    List<ModuleDto> modules;
}