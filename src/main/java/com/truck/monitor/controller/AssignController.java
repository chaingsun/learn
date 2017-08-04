package com.truck.monitor.controller;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Assign;
import com.truck.monitor.domain.Motorcade;
import com.truck.monitor.service.AssignService;
import com.truck.monitor.service.HDService;

@RestController
@RequestMapping("assigns")
public class AssignController extends HDController<Assign> {

	@Autowired
	private AssignService service;

	public HDService<Assign> getService() {
		return service;
	}

	@RequestMapping(value = "/motorcade/{motorcade}/cancel", method = RequestMethod.DELETE)
	public Result cancel(@PathVariable Long motorcade) {
		service.deleteByMotorcadeAndDay(new Motorcade(motorcade), DateFormatUtils.format(Calendar.getInstance(), Assign.PATTERN));
		return SUCCESS;
	}

	/**
	 * 取消具体某个指派数据[不能用POST方式].
	 * 
	 * @param assign
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public Result cancel(@RequestBody Assign assign) {
		service.cancel(assign);
		return SUCCESS;
	}

}
