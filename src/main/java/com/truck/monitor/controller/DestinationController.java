package com.truck.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Destination;
import com.truck.monitor.service.DestinationService;
import com.truck.monitor.service.HDService;

@RestController
@RequestMapping("destinations")
public class DestinationController extends HDController<Destination> {

	@Autowired
	private DestinationService service;

	public HDService<Destination> getService() {
		return service;
	}
}
