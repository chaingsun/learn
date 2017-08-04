package com.truck.monitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Transport;

public interface BehaviorRepository extends HDRepository<Behavior> {

	/**
	 * 查询该次运输发生的行为.
	 * 
	 * @param transport
	 * @return
	 */
	@Query("select new Behavior(b.id, b.time, b.longitude, b.latitude, b.type) from Behavior b where b.transport = ?1 order by b.time desc")
	List<Behavior> findByTransportOrderByTimeDesc(Transport transport);

}
