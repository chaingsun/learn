package com.truck.monitor.repository;

import java.util.List;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Materiel;
import com.truck.monitor.domain.MaterielAmount;

public interface MaterielAmountRepository extends HDRepository<MaterielAmount> {

	List<MaterielAmount> findByMaterielAndSiteAndYearOrderByMonthAsc(Materiel materiel, ConstructionSite site, int year);

	MaterielAmount findByMaterielAndSiteAndYearAndMonth(Materiel materiel, ConstructionSite site, int year, int month);

}
