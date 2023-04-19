/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.xact.assessment.exceptions.InvalidContributorException;
import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
@Introspected
public class ContributorRequest {
    private List<ContributorDto> contributors;

    public void validate(String emailPattern){
        for(ContributorDto eachContributor : contributors){
            if((!eachContributor.isValid(emailPattern)) || (isAlreadyAContributor(eachContributor))){
                throw new InvalidContributorException("Invalid Request");
            }
        }
    }

    private boolean isAlreadyAContributor(ContributorDto eachContributor) {
        return contributors.stream().filter(contributor->contributor.getUserEmail().equals(eachContributor.getUserEmail())).count()>1;
    }

}
