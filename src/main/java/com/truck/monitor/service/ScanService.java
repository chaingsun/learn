package com.truck.monitor.service;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Scan;

public interface ScanService extends HDService<Scan> {

	Scan findByBehavior(Behavior behavior);
}
