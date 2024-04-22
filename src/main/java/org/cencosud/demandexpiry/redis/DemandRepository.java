package org.cencosud.demandexpiry.redis;

import org.cencosud.demandexpiry.Demand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandRepository extends CrudRepository<Demand, String> {
}
