package com.truck.monitor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Transport;
import com.truck.monitor.vo.TransportStatis;

public interface TransportService extends HDService<Transport> {

	TransportStatis statis(Long userId, Date startDate, Date endDate);

	Transport findByDriverAndStatusNot(Driver driver, Behavior.Type status);

	List<Map<String, Object>> findStatisticsByYearsAndMonths(Driver driver, Integer[] years, Integer[] months);

}
