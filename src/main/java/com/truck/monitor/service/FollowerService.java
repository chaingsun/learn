package com.truck.monitor.service;

import com.truck.monitor.domain.Follower;

public interface FollowerService {

	Follower findOne(String openid);

	Follower save(Follower follower);

}
