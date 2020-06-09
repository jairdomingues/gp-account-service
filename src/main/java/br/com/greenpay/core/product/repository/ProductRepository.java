package br.com.greenpay.core.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.greenpay.core.product.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

	@Query("SELECT p FROM Product p WHERE p.partner.id=:partnerId")
	List<Product> findAllProductsByPartner(@Param("partnerId") Long partnerId);

}
