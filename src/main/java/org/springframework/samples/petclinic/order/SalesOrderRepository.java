package org.springframework.samples.petclinic.order;

import org.springframework.data.repository.CrudRepository;

public interface SalesOrderRepository extends CrudRepository<SalesOrder, Long> {

}

