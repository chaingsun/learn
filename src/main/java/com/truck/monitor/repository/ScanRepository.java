package com.truck.monitor.repository;

import org.springframework.data.jpa.repository.Query;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Scan;

public interface ScanRepository extends HDRepository<Scan> {

	@Query("select new Scan(c.id, c.time, c.distance) from Scan c where c.behavior = ?1")
	Scan findByBehavior(Behavior behavior);
}
