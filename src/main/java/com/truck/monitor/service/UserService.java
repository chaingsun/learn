package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.Follower;
import com.truck.monitor.domain.User;

public interface UserService extends HDService<User> {

	User findByFollower(Follower follower);

	User findByNameAndPhoneAndCode(String name, String phone, String code);

	List<User> findByPhone(String phone);
}
