package com.truck.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Motorcade;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.MotorcadeRepository;

@Service
public class MotorcadeServiceImpl extends HDServiceImpl<Motorcade> implements MotorcadeService{

	@Autowired
	private MotorcadeRepository repository;

	public HDRepository<Motorcade> getRepository() {
		return repository;
	}
}
