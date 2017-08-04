package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.Assign;
import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Motorcade;

public interface AssignService extends HDService<Assign> {

	Assign findByDriverAndDay(Driver driver, String day);

	void cancel(Assign assign);

	List<Driver> findAssignedDrivers4MotorcadeByDay(ConstructionSite site, Motorcade motorcade, String day);

	List<Driver> findAssignedDrivers4MotorcadeByDay(Motorcade motorcade, String day);

	List<Assign> findAssignsByDay(Motorcade motorcade, String day);

	List<Assign> findByMotorcadeAndDay(Motorcade motorcade, String day);

	void deleteByMotorcadeAndDay(Motorcade motorcade, String day);
}
