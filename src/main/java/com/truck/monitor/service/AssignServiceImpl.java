package com.truck.monitor.service;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.Assign;
import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Motorcade;
import com.truck.monitor.repository.AssignRepository;

import me.chanjar.weixin.common.util.StringUtils;

@Service
@Transactional
public class AssignServiceImpl extends HDServiceImpl<Assign> implements AssignService {

	@Autowired
	private AssignRepository repository;

	public AssignRepository getRepository() {
		return repository;
	}

	public Assign findByDriverAndDay(Driver driver, String day) {
		return repository.findByDriverAndDay(driver, day);
	}

	public Assign add(Assign assign) {
		Calendar beginCal = Calendar.getInstance();
		if (assign.getBegin() != null) {

			beginCal.setTime(assign.getBegin());
		}

		Calendar endCal = Calendar.getInstance();
		if (assign.getEnd() != null) {

			endCal.setTime(assign.getEnd());
		}
		if (beginCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			throw new RuntimeException("参数错误.");
		}
		while (true) {
			String day = DateFormatUtils.format(beginCal, Assign.PATTERN);
			if (repository.findByDriverAndDay(assign.getDriver(), day) == null) {
				super.add(new Assign(assign.getSite(), assign.getDriver(), assign.getAssigner(), day));
				if (day.equals(DateFormatUtils.format(endCal, Assign.PATTERN))) {
					break;
				}
			}
			beginCal.add(Calendar.DATE, 1);
		}
		return null;
	}

	public List<Driver> findAssignedDrivers4MotorcadeByDay(ConstructionSite site, Motorcade motorcade, String day) {
		return repository.findAssignedDrivers4MotorcadeByDay(site, motorcade, day);
	}

	public List<Driver> findAssignedDrivers4MotorcadeByDay(Motorcade motorcade, String day) {
		return repository.findAssignedDrivers4MotorcadeByDay(motorcade, day);
	}

	public void cancel(Assign assign) {
		Assign entity = repository.findByDriverAndDay(assign.getDriver(),
				StringUtils.isEmpty(assign.getDay()) ? DateFormatUtils.format(Calendar.getInstance(), Assign.PATTERN) : assign.getDay());
		if (entity != null) {
			repository.delete(entity);
		}
	}

	@Override
	public List<Assign> findAssignsByDay(Motorcade motorcade, String day) {
		return repository.findByMotorcadeAndDay(motorcade, day);
	}

	@Override
	public List<Assign> findByMotorcadeAndDay(Motorcade motorcade, String day) {
		return repository.findByMotorcadeAndDay(motorcade, day);
	}

	public void deleteByMotorcadeAndDay(Motorcade motorcade, String day) {
		List<Assign> assigns = repository.findByMotorcadeAndDay(motorcade, day);
		repository.delete(assigns);
	}
}
