package com.truck.monitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.OpsUser;
import com.truck.monitor.service.OpsUserService;

@RestController
@RequestMapping("opsusers")
public class OpsUserController extends HDController<OpsUser> {

	private OpsUserService service;

	public OpsUserService getService() {
		return service;
	}

}
