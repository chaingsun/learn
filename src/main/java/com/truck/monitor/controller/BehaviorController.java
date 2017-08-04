package com.truck.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Behavior.Type;
import com.truck.monitor.service.BehaviorService;

@RestController
@RequestMapping("behaviors")
public class BehaviorController extends HDController<Behavior> {

	@Autowired
	private BehaviorService service;

	public BehaviorService getService() {
		return service;
	}

	/**
	 * 主管审核.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/auditing", method = RequestMethod.POST)
	public Result auditing(@RequestBody Behavior behavior) {
		service.auditing(behavior);
		return SUCCESS;
	}

	/**
	 * 司机入场.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/entrance", method = RequestMethod.POST)
	public Result entrance(@RequestBody Behavior behavior) {
		behavior.setType(Type.IN);
		service.add(behavior);
		return new Result(Boolean.TRUE, "进场成功.");
	}

}
