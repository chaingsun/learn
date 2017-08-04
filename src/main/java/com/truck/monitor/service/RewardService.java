package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Destination;
import com.truck.monitor.domain.Materiel;
import com.truck.monitor.domain.Reward;

public interface RewardService {

	void modify(List<Reward> rewards);

	List<Reward> findBySiteAndDestination(ConstructionSite site, Destination destination);

	void deleteByDestination(Destination destination);

	Reward findBySiteAndDestinationAndMateriel(ConstructionSite site, Destination destination, Materiel materiel);
}
