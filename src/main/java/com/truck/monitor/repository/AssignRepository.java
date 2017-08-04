package com.truck.monitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.truck.monitor.domain.Assign;
import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Motorcade;

public interface AssignRepository extends HDRepository<Assign> {

	Assign findByDriverAndDay(Driver driver, String day);

	@Query("select new Driver(d.id, d.phone, d.name) from Assign a join a.driver d where a.site = ?1 and d.motorcade = ?2 and a.day = ?3")
	List<Driver> findAssignedDrivers4MotorcadeByDay(ConstructionSite site, Motorcade motorcade, String day);

	@Query("select new Driver(d.id, d.phone, d.name, d.licensePlate, d.carNo, s.name) from Assign a join a.driver d join a.site s where d.motorcade = ?1 and a.day = ?2")
	List<Driver> findAssignedDrivers4MotorcadeByDay(Motorcade motorcade, String day);

	@Query("select a from Assign a join a.driver d join a.site s where d.motorcade = ?1 and a.day = ?2")
	List<Assign> findByMotorcadeAndDay(Motorcade motorcade, String day);

}
