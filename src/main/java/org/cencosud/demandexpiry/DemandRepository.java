package org.cencosud.demandexpiry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends CrudRepository<Demand, String> {
}
