package com.truck.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Destination;
import com.truck.monitor.repository.DestinationRepository;
import com.truck.monitor.repository.HDRepository;

@Service
public class DestinationServiceImpl extends HDServiceImpl<Destination> implements DestinationService {

	@Autowired
	private DestinationRepository repository;

	@Autowired
	private RewardService rewardService;

	public HDRepository<Destination> getRepository() {
		return repository;
	}

	@Override
	public void delete(Long id) {
		rewardService.deleteByDestination(new Destination(id));
		super.delete(id);
	}

}
