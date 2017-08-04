package com.truck.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Destination;
import com.truck.monitor.domain.Reward;
import com.truck.monitor.service.RewardService;

@RestController
@RequestMapping("rewards")
public class RewardController {

	@Autowired
	private RewardService service;

	public RewardService getService() {
		return service;
	}

	@RequestMapping(value = "/site/{site}/destination/{destination}", method = RequestMethod.GET)
	public List<Reward> findBySiteAndDestination(@PathVariable Long site, @PathVariable Long destination) {
		return service.findBySiteAndDestination(new ConstructionSite(site), new Destination(destination));
	}

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public HDController.Result modify(@RequestBody List<Reward> rewards) {
		service.modify(rewards);
		return HDController.SUCCESS;
	}

}
