package com.truck.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Destination;
import com.truck.monitor.domain.Materiel;
import com.truck.monitor.domain.Reward;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.RewardRepository;

@Service
public class RewardServiceImpl implements RewardService {

	@Autowired
	private RewardRepository repository;

	@Autowired
	private MaterielService materielService;

	public HDRepository<Reward> getRepository() {
		return repository;
	}

	public List<Reward> findBySiteAndDestination(ConstructionSite site, Destination destination) {
		List<Reward> rewards = repository.findBySiteAndDestination(site, destination);
		if (rewards.size() == 0) {
			List<Materiel> materiels = materielService.findAll();
			for (Materiel materiel : materiels) {
				rewards.add(new Reward(site, destination, materiel));
			}
			return repository.save(rewards);
		}
		return rewards;
	}

	public Reward findBySiteAndDestinationAndMateriel(ConstructionSite site, Destination destination, Materiel materiel) {
		return repository.findBySiteAndDestinationAndMateriel(site, destination, materiel);
	}

	public void modify(List<Reward> rewards) {
		for (Reward item : rewards) {
			if (item.getId() != null) {
				Reward reward = repository.findOne(item.getId());
				reward.setReward(item.getReward());
				repository.save(reward);
			}
		}
	}

	public void deleteByDestination(Destination destination) {
		repository.deleteByDestination(destination);
	}

}
