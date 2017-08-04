package com.truck.monitor.weixin.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weixin")
public class Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3644771321449254407L;

	private String token;

	private String appId;

	private String appSecret;

	private String encodingAesKey;

	private boolean messageEncrypt = false; // 消息加密与否

	private String host;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getEncodingAesKey() {
		return encodingAesKey;
	}

	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}

	public boolean isMessageEncrypt() {
		return messageEncrypt;
	}

	public void setMessageEncrypt(boolean messageEncrypt) {
		this.messageEncrypt = messageEncrypt;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
