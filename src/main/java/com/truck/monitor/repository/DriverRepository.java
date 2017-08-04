package com.truck.monitor.repository;

import java.util.List;

import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Follower;
import com.truck.monitor.domain.Motorcade;

public interface DriverRepository extends HDRepository<Driver> {
	List<Driver> findByMotorcade(Motorcade motorcade);

	Driver findDriverByFollower(Follower follower);
}
