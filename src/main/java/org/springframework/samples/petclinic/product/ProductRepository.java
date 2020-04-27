package org.springframework.samples.petclinic.product;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.partnerId=:partnerId")
	List<Product> findAllProductsByPartner(@Param("partnerId") Long partnerId);

}
