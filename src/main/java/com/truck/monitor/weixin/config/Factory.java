package com.truck.monitor.weixin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

@Configuration
public class Factory {

	@Autowired
	private Config wxMpConfig;

	@Bean
	public WxMpConfigStorage buildWxMpConfigStorage() {
		WxMpInMemoryConfigStorage storage = new WxMpInMemoryConfigStorage();
		storage.setAppId(wxMpConfig.getAppId());
		storage.setSecret(wxMpConfig.getAppSecret());
		storage.setToken(wxMpConfig.getToken());
		storage.setAesKey(wxMpConfig.getEncodingAesKey());
		return storage;
	}

	@Bean
	public WxMpService buildWxMpService() {
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(buildWxMpConfigStorage());
		return wxMpService;
	}

}
