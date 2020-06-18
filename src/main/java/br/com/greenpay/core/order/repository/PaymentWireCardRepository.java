package br.com.greenpay.core.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.greenpay.core.order.model.PaymentWireCard;

public interface PaymentWireCardRepository extends CrudRepository<PaymentWireCard, Long> {

	abstract List<PaymentWireCard> findByStatus(String status);

//	abstract Optional<PaymentWireCard> findBy_Ìd(String _id);

	@Query("SELECT p FROM PaymentWireCard p WHERE p._id=:id")
	PaymentWireCard findByPaymentWireCard(@Param("id") String id);

}
