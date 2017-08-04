package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Materiel;
import com.truck.monitor.domain.MaterielAmount;

public interface MaterielAmountService extends HDService<MaterielAmount> {

	List<MaterielAmount> findByMaterielAndSiteAndYearOrderByMonthAsc(Materiel materiel, ConstructionSite site, int year);

	MaterielAmount findByMaterielAndSiteAndYearAndMonth(Materiel materiel, ConstructionSite site, int year, int month);

	void modify(List<MaterielAmount> amounts);

}
