package com.truck.monitor.controller;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Assign;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.service.AssignService;
import com.truck.monitor.service.DriverService;

@RestController
@RequestMapping("drivers")
public class DriverController extends HDController<Driver> {

	@Autowired
	private DriverService service;

	@Autowired
	private AssignService assignService;

	public DriverService getService() {
		return service;
	}

	@RequestMapping(value = "/{id}/assigned", method = RequestMethod.GET)
	public Assign findTodayAssigned(@PathVariable Long id) {
		return assignService.findByDriverAndDay(new Driver(id), DateFormatUtils.format(Calendar.getInstance(), Assign.PATTERN));
	}

}
