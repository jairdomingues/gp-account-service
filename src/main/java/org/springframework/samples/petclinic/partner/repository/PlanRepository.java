package org.springframework.samples.petclinic.partner.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.partner.model.Plan;

public interface PlanRepository extends CrudRepository<Plan, Long> {

}
