package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Motorcade;

public interface DriverService extends HDService<Driver> {

	List<Driver> findByMotorcade(Motorcade motorcade);

}
