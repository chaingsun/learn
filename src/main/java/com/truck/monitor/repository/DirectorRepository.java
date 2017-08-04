package com.truck.monitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.truck.monitor.domain.Director;

public interface DirectorRepository extends HDRepository<Director> {

	@Query("select new Director(id,phone,name) from Director where status = 1")
	List<Director> findAll();
}
