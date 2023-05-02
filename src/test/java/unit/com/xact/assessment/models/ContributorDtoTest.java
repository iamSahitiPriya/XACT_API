/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;


import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRequest;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.exceptions.InvalidContributorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ContributorDtoTest {

    @Test
    void shouldCheckValidity() {
        ContributorRequest contributorRequest=new ContributorRequest();
        ContributorDto contributorDto = new ContributorDto();
        contributorDto.setUserEmail("abc@gmail.com");
        contributorDto.setRole(ContributorRole.AUTHOR);
        contributorRequest.setContributors(Collections.singletonList(contributorDto));

        Assertions.assertThrows(InvalidContributorException.class, ()-> contributorRequest.validate( "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$"));
    }
}
