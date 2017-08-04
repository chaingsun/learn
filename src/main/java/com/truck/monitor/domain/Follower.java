package com.truck.monitor.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 关注该工作号的微信用户.
 * 
 * @author lzq
 *
 */
@Table
@Entity
public class Follower implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1917528520114894699L;

	public Follower() {

	}

	public Follower(String openid) {
		this.setOpenid(openid);
	}

	/**
	 * 微信openid.
	 */
	@Id
	@Column(length = 32)
	private String openid;

	/**
	 * 微信头像.
	 */
	private String headImgUrl;

	private String nickname;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
