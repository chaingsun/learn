package com.truck.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Materiel;
import com.truck.monitor.domain.MaterielAmount;
import com.truck.monitor.service.MaterielAmountService;
import com.truck.monitor.service.MaterielService;

@RestController
@RequestMapping("materiels")
public class MaterielController extends HDController<Materiel> {

	@Autowired
	private MaterielService service;

	@Autowired
	private MaterielAmountService materielAmountService;

	public MaterielService getService() {
		return service;
	}

	@RequestMapping(value = "/{id}/site/{site}/year/{year}/amounts", method = RequestMethod.GET)
	public List<MaterielAmount> findByMaterielAndSiteAndYearOrderByMonthAsc(@PathVariable Long id, @PathVariable Long site, @PathVariable Integer year) {
		return materielAmountService.findByMaterielAndSiteAndYearOrderByMonthAsc(new Materiel(id), new ConstructionSite(site), year);
	}
}
