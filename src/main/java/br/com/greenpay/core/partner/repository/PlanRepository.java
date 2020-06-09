package br.com.greenpay.core.partner.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.greenpay.core.partner.model.Plan;

public interface PlanRepository extends CrudRepository<Plan, Long> {

}
