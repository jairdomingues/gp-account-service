package org.springframework.samples.petclinic.system;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends CrudRepository<Account, Long> {

	@Query("SELECT a FROM Account a WHERE a.cardNumber=:cardNumber")
	Account findAccountByCardNumber(@Param("cardNumber") String cardNumber);

	@Query("SELECT a FROM Account a WHERE a.currentAccount=:currentAccount AND a.agency=:agency AND a.bankCode=:bankCode")
	Account findAccountByCurrentAccount(@Param("currentAccount") String currentAccount, @Param("agency") String agency,
			@Param("bankCode") String bankCode);

	@Query("SELECT a FROM Account a WHERE a.hashCard=:hashCard")
	Account findAccountByHashCard(@Param("hashCard") String hashCard);
	
}
