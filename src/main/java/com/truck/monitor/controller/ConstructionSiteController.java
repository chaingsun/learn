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
import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.ConstructionSiteQrCode;
import com.truck.monitor.domain.Driver;
import com.truck.monitor.domain.Motorcade;
import com.truck.monitor.service.AssignService;
import com.truck.monitor.service.ConstructionSiteQrCodeService;
import com.truck.monitor.service.ConstructionSiteService;

/**
 * 工地.
 * 
 * @author lzq
 *
 */
@RestController
@RequestMapping("constructionsites")
public class ConstructionSiteController extends HDController<ConstructionSite> {

	@Autowired
	private ConstructionSiteService service;

	@Autowired
	private ConstructionSiteQrCodeService constructionSiteQrCodeService;

	@Autowired
	private AssignService assignService;

	public ConstructionSiteService getService() {
		return service;
	}

	@RequestMapping(value = "/{id}/qrcodes", method = RequestMethod.GET)
	public List<ConstructionSiteQrCode> findQrCodes(@PathVariable Long id) {
		return constructionSiteQrCodeService.findByConstructionSite(new ConstructionSite(id));
	}

	@RequestMapping(value = "/{id}/motorcade/{motorcade}/assigned/drivers", method = RequestMethod.GET)
	public List<Driver> findAssignedDrivers4MotorcadeByDay(@PathVariable Long id, @PathVariable Long motorcade, String day) {
		if (StringUtils.isEmpty(day)) {
			day = DateFormatUtils.format(Calendar.getInstance(), Assign.PATTERN);
		}
		return assignService.findAssignedDrivers4MotorcadeByDay(new ConstructionSite(id), new Motorcade(motorcade), day);
	}
}
