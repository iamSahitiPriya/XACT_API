package com.xact.assessment.repositories;

import com.xact.assessment.models.Account;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    @Executable
    @Query("SELECT account FROM Account account WHERE lower( account.name) LIKE lower( CONCAT(CONCAT('%',:name),'%')) OR lower( account.name) LIKE lower(CONCAT(:name,'%')) OR lower(account.name) LIKE lower(CONCAT('%',:name))")
    List<Account> findAccount(@Parameter("name")String name);
}
