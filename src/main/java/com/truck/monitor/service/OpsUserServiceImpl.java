package com.truck.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.OpsUser;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.OpsUserRepository;

@Service
public class OpsUserServiceImpl extends HDServiceImpl<OpsUser> implements OpsUserService {

	@Autowired
	public OpsUserRepository repository;

	public HDRepository<OpsUser> getRepository() {
		return repository;
	}

	public OpsUser findByPhoneAndPwd(String phone, String pwd) {
		return repository.findByPhoneAndPwd(phone, pwd);
	}
}
