package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Transport;

public interface BehaviorService extends HDService<Behavior> {

	/**
	 * 查询该运输发生的行为.
	 * 
	 * @param transport
	 * @return
	 */
	List<Behavior> findByTransportOrderByTimeDesc(Transport transport);

	void auditing(Behavior behavior);

}
