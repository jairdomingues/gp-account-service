package org.springframework.samples.petclinic.order.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.order.model.AccountWireCard;

public interface AccountWireCardRepository extends CrudRepository<AccountWireCard, Long> {

}

