package com.truck.monitor.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Motorcade;
import com.truck.monitor.domain.User;
import com.truck.monitor.repository.DriverRepository;
import com.truck.monitor.repository.HDRepository;

@Service
public class DriverServiceImpl extends HDServiceImpl<Driver> implements DriverService {

	@Autowired
	private DriverRepository repository;

	@Autowired
	private UserService userService;

	public HDRepository<Driver> getRepository() {
		return repository;
	}

	public List<Driver> findByMotorcade(Motorcade motorcade) {
		return repository.findByMotorcade(motorcade);
	}

	public Driver add(Driver driver) {

		List<User> users = userService.findByPhone(driver.getPhone());
		if (users.size() > 0) {
			throw new RuntimeException("手机号[" + driver.getPhone() + "]已存在.");
		}
		driver.setRegTime(new Date());
		super.add(driver);
		driver.setCode("SJ" + String.format("%04d", driver.getId()));
		super.modify(driver);
		return driver;
	}

	@Override
	public void modify(Driver driver) {
		List<User> users = userService.findByPhone(driver.getPhone());
		for (User user : users) {
			if (!user.getId().equals(driver.getId())) {
				throw new RuntimeException("手机号[" + driver.getPhone() + "]已存在.");
			}
		}
		super.modify(driver);
	}

}
