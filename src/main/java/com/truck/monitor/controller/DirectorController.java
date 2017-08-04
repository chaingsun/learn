package com.truck.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Director;
import com.truck.monitor.service.BehaviorService;
import com.truck.monitor.service.DirectorService;

@RestController
@RequestMapping("directors")
public class DirectorController extends HDController<Director> {

	@Autowired
	private DirectorService service;

	@Autowired
	private BehaviorService behaviorService;

	public DirectorService getService() {
		return service;
	}

	@RequestMapping(value = "/{id}/behaviors/auditing", method = RequestMethod.GET)
	public Page<Behavior> findBehaviors2Auditing(@PathVariable Long id, @PageableDefault(sort = { "id" }, page = 0, size = 20, direction = Direction.DESC) Pageable pageable) {
		return behaviorService.findByPage(new Behavior(new Director(id)), pageable);
	}

}
