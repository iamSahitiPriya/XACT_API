package com.xact.assessment.repositories;

import com.xact.assessment.models.Accounts;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AccountRepository extends CrudRepository<Accounts, String> {
}
