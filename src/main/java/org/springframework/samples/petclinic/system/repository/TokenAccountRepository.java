package org.springframework.samples.petclinic.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.system.model.TokenAccount;

public interface TokenAccountRepository extends CrudRepository<TokenAccount, Long> {

	@Query("SELECT t FROM TokenAccount t WHERE t.uuid=:uuid AND t.valid='true'")
	abstract Optional<TokenAccount> findByUuidValid(String uuid);

}
