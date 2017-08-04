package com.truck.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Follower;
import com.truck.monitor.repository.FollowerRepository;

@Service
public class FollowerServiceImpl implements FollowerService {

	@Autowired
	private FollowerRepository repository;

	public Follower save(Follower follower) {
		return repository.save(follower);
	}

	public Follower findOne(String openid) {
		return repository.findOne(openid);
	}

}
