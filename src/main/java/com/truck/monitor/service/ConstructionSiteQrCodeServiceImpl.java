package com.truck.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truck.monitor.domain.ConstructionSite;
import com.truck.monitor.domain.ConstructionSiteQrCode;
import com.truck.monitor.repository.ConstructionSiteQrCodeRepository;
import com.truck.monitor.repository.HDRepository;
import com.truck.monitor.weixin.config.Config;

import me.chanjar.weixin.mp.api.WxMpService;

@Service
public class ConstructionSiteQrCodeServiceImpl extends HDServiceImpl<ConstructionSiteQrCode> implements ConstructionSiteQrCodeService {

	@Autowired
	private ConstructionSiteQrCodeRepository repository;

	@Autowired
	private QRCodeService qrCodeService;

	@Autowired
	private Config config;

	@Autowired
	private WxMpService wxMpService;

	public HDRepository<ConstructionSiteQrCode> getRepository() {
		return repository;
	}

	public List<ConstructionSiteQrCode> findByConstructionSite(ConstructionSite constructionSite) {
		return repository.findByConstructionSite(constructionSite);
	}

	public ConstructionSiteQrCode add(ConstructionSiteQrCode qrCode) {
		super.add(qrCode);
		try {
			qrCode.setPath(qrCodeService.getStoragePath() + java.io.File.separator + qrCodeService
					.encode(wxMpService.oauth2buildAuthorizationUrl(config.getHost() + "qrcodes/" + qrCode.getId() + "/scan", "snsapi_base", "SCAN"), null, null).getName());
			super.modify(qrCode);
		} catch (Exception e) {
			throw new RuntimeException("工地二维码生成失败.");
		}
		return qrCode;
	}

	public void delete(Long id) {
		ConstructionSiteQrCode qrCode = this.findOne(id);
		if (qrCode.getActivated() != null && qrCode.getActivated()) {
			throw new RuntimeException("已激活的二维码不能删除.");
		}
		repository.delete(qrCode);
	}

}
