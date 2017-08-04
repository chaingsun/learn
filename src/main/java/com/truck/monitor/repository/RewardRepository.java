package com.truck.monitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Destination;
import com.truck.monitor.domain.Materiel;
import com.truck.monitor.domain.Reward;

public interface RewardRepository extends HDRepository<Reward> {

	@Query("select new Reward(r.id, m, r.reward) from Reward r join r.materiel m where r.site = ?1 and r.destination = ?2")
	List<Reward> findBySiteAndDestination(ConstructionSite site, Destination destination);

	void deleteByDestination(Destination destination);

	Reward findBySiteAndDestinationAndMateriel(ConstructionSite site, Destination destination, Materiel materiel);
}
