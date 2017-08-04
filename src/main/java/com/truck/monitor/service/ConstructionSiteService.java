package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.Director;

public interface ConstructionSiteService extends HDService<ConstructionSite> {

	List<ConstructionSite> findByDirector(Director director);
}
