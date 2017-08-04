package com.truck.monitor.service;

import java.util.List;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.ConstructionSiteQrCode;

public interface ConstructionSiteQrCodeService extends HDService<ConstructionSiteQrCode> {
	/**
	 * 获取工地对应的二维码.
	 * 
	 * @param constructionSite
	 * @return
	 */
	List<ConstructionSiteQrCode> findByConstructionSite(ConstructionSite constructionSite);
}
