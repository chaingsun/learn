package com.truck.monitor.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.Behavior;
import com.truck.monitor.domain.Destination;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Reward;
import com.truck.monitor.domain.Transport;
import com.truck.monitor.service.BehaviorService;
import com.truck.monitor.service.RewardService;
import com.truck.monitor.service.ScanService;
import com.truck.monitor.service.TransportService;
import com.truck.monitor.vo.TransportStatis;

@RestController
@RequestMapping("transports")
public class TransportController extends HDController<Transport> {

	@Autowired
	private TransportService service;

	@Autowired
	private BehaviorService behaviorService;

	@Autowired
	private ScanService scanService;

	@Autowired
	private RewardService rewardService;

	public TransportService getService() {
		return service;
	}

	@RequestMapping(value = "/{id}/destination/{destination}", method = RequestMethod.PUT)
	public Result destinate(@PathVariable Long id, @PathVariable Long destination) {
		Transport transport = service.findOne(id);
		transport.setDestination(new Destination(destination));
		Reward reward = rewardService.findBySiteAndDestinationAndMateriel(transport.getConstructionSite(), transport.getDestination(), transport.getMateriel());
		if (reward != null) {
			transport.setReward(reward.getReward());
		} else {
			transport.setReward(null);
		}
		service.modify(transport);
		return new Result(Boolean.TRUE, transport.getReward());
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public List<Map<String, Object>> findStatisticsByYearsAndMonths(Integer[] years, Integer[] months, Long driver) {
		if ((years == null || years.length == 0) || (months == null || months.length == 0)) {
			throw new RuntimeException("参数错误.");
		}
		return service.findStatisticsByYearsAndMonths(new Driver(driver), years, months);
	}

	@RequestMapping(value = "/{id}/behaviors", method = RequestMethod.GET)
	private List<Behavior> findBehaviors(@PathVariable Long id) {
		List<Behavior> behaviors = behaviorService.findByTransportOrderByTimeDesc(new Transport(id));
		for (Behavior behavior : behaviors) {
			behavior.setScan(scanService.findByBehavior(behavior));
		}
		return behaviors;
	}

	/**
	 * 卸货.
	 * 
	 * @param id
	 */
	@RequestMapping(value = "/{id}/discharge", method = RequestMethod.POST)
	public Result discharge(@PathVariable Long id, @RequestBody Behavior behavior) {
		behavior.setType(Behavior.Type.DISCHARGE);
		behavior.setTransport(new Transport(id));
		behaviorService.add(behavior);
		return new Result(Boolean.TRUE, "卸货成功.");
	}

	/**
	 * 司机离场.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{id}/out", method = RequestMethod.POST)
	public Result out(@PathVariable Long id, @RequestBody Behavior behavior) {
		behavior.setType(Behavior.Type.OUT);
		behavior.setTransport(new Transport(id));
		behaviorService.add(behavior);
		return new Result(Boolean.TRUE, "退场成功.");
	}

	@RequestMapping(value = "{userId}/statis", method = RequestMethod.POST)
	public TransportStatis statis(@PathVariable(value = "userId") Long userId, Date startDate, Date endDate) {
		try {
			System.out.println("startDate:" + startDate + ", endDate:" + endDate);
			return service.statis(userId, startDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			return new TransportStatis();
		}
	}
}
