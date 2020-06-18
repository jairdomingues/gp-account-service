package br.com.greenpay.core.system.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.greenpay.core.system.model.Recurrence;

public interface RecurrenceRepository extends CrudRepository<Recurrence, Long> {

}
