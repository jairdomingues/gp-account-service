package br.com.greenpay.core.order.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.greenpay.core.order.model.SalesOrder;

public interface SalesOrderRepository extends CrudRepository<SalesOrder, Long> {

}

