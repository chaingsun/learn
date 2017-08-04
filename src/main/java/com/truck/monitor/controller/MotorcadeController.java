package com.truck.monitor.controller;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Assign;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Motorcade;
import com.truck.monitor.service.AssignService;
import com.truck.monitor.service.DriverService;
import com.truck.monitor.service.MotorcadeService;

@RestController
@RequestMapping("motorcades")
public class MotorcadeController extends HDController<Motorcade> {

	@Autowired
	private MotorcadeService service;

	@Autowired
	private DriverService driverService;

	@Autowired
	private AssignService assignService;

	public MotorcadeService getService() {
		return service;
	}

	@RequestMapping(value = "/{id}/drivers", method = RequestMethod.GET)
	public List<Driver> findDrivers(@PathVariable Long id) {
		return driverService.findByMotorcade(new Motorcade(id));
	}

	@RequestMapping(value = "/{id}/assigned/drivers", method = RequestMethod.GET)
	public List<Driver> findAssignedDrivers4MotorcadeByDay(@PathVariable Long id, String day) {
		if (StringUtils.isEmpty(day)) {
			day = DateFormatUtils.format(Calendar.getInstance(), Assign.PATTERN);
		}
		return assignService.findAssignedDrivers4MotorcadeByDay(new Motorcade(id), day);
	}

	@RequestMapping(value = "/{id}/assigns", method = RequestMethod.GET)
	public List<Assign> findAssignsByDay(@PathVariable Long id ,String day) {
		if (StringUtils.isEmpty(day)) {
			day = DateFormatUtils.format(Calendar.getInstance(), Assign.PATTERN);
		}
		return assignService.findAssignsByDay(new Motorcade(id), day);
	}
}
