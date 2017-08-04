package com.truck.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Follower;
import com.truck.monitor.domain.User;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.UserRepository;

@Service
public class UserServiceImpl extends HDServiceImpl<User> implements UserService {

	@Autowired
	private UserRepository repository;

	public HDRepository<User> getRepository() {
		return repository;
	}

	public User findByFollower(Follower follower) {
		return repository.findByFollower(follower);
	}

	public User findByNameAndPhoneAndCode(String name, String phone, String code) {
		return repository.findByNameAndPhoneAndCode(name, phone, code);
	}

	@Override
	public List<User> findByPhone(String phone) {
		return repository.findByPhone(phone);
	}

}
