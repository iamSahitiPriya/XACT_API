package com.xact.assessment.repositories;

import com.xact.assessment.models.AccessControlList;
import com.xact.assessment.models.AccessControlRoles;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface AccessControlRepository extends CrudRepository<AccessControlList,String> {
    @Executable
    @Query("SELECT acl.accessControlRoles FROM AccessControlList acl WHERE acl.email=:email")
    Optional<AccessControlRoles> getAccessControlRolesByEmail(String email);

    @Executable
    boolean existsByEmail(@NotNull String email);

}
