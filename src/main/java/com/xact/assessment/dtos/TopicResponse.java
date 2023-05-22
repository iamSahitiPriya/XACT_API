/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TopicResponse {
    private Integer topicId;
    private String topicName;
    private Integer moduleId;
    private Integer categoryId;
    private Date updatedAt;
    private String comments;
    private boolean active;
    private boolean isTopicLevelReference;
}
