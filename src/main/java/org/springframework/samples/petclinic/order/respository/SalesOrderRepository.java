package org.springframework.samples.petclinic.order.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.order.model.SalesOrder;

public interface SalesOrderRepository extends CrudRepository<SalesOrder, Long> {

}

