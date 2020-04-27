package org.springframework.samples.petclinic.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.system.Account;

public interface ProductRepository extends CrudRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.partnerId=:partnerId")
	Account findAllProductsByPartner(@Param("partnerId") Long partnerId);
	
	
}

