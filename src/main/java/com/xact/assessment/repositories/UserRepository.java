/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.UserInfo;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, String> {

    @Executable
    @Query("select tlu from UserInfo tlu where tlu.email in :users ")
    List<UserInfo> findByUsers(Set<String> users);

}
