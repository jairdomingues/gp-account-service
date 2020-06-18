package br.com.greenpay.core.partner.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.greenpay.core.partner.model.ReleaseHistory;

public interface ReleaseHistoryRepository extends CrudRepository<ReleaseHistory, Long> {

}
