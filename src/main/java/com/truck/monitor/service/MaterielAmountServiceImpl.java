package com.truck.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Materiel;
import com.truck.monitor.domain.MaterielAmount;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.repository.MaterielAmountRepository;

@Service
public class MaterielAmountServiceImpl extends HDServiceImpl<MaterielAmount> implements MaterielAmountService {

	@Autowired
	private MaterielAmountRepository repository;

	public HDRepository<MaterielAmount> getRepository() {
		return repository;
	}

	public List<MaterielAmount> findByMaterielAndSiteAndYearOrderByMonthAsc(Materiel materiel, ConstructionSite site, int year) {
		List<MaterielAmount> amounts = repository.findByMaterielAndSiteAndYearOrderByMonthAsc(materiel, site, year);
		if (amounts.size() != 12) {
			for (int month = 1; month <= 12; month++) {
				amounts.add(new MaterielAmount(site, materiel, year, month));
			}
			repository.save(amounts);
		}
		return amounts;
	}

	public void modify(List<MaterielAmount> amounts) {
		repository.save(amounts);
	}

	public MaterielAmount findByMaterielAndSiteAndYearAndMonth(Materiel materiel, ConstructionSite site, int year, int month) {
		return repository.findByMaterielAndSiteAndYearAndMonth(materiel, site, year, month);
	}

}
