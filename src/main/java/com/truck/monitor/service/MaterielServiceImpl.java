package com.truck.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Materiel;
import com.truck.monitor.repository.MaterielRepository;
import com.truck.monitor.repository.HDRepository;

@Service
public class MaterielServiceImpl extends HDServiceImpl<Materiel> implements MaterielService {

	@Autowired
	private MaterielRepository repository;

	public HDRepository<Materiel> getRepository() {
		return repository;
	}
}
