package com.truck.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Director;
import com.truck.monitor.repository.ConstructionSiteRepository;

@Service
public class ConstructionSiteServiceImpl extends HDServiceImpl<ConstructionSite> implements ConstructionSiteService {

	@Autowired
	private ConstructionSiteRepository repository;

	public ConstructionSiteRepository getRepository() {
		return repository;
	}

	@Override
	public List<ConstructionSite> findByDirector(Director director) {
		return repository.findByDirector(director);
	}
}
