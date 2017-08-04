package com.truck.monitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 工地二维码.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class ConstructionSiteQrCode {

	public ConstructionSiteQrCode(Long id) {
		this.id = id;
	}

	public ConstructionSiteQrCode() {

	}

	public ConstructionSiteQrCode(ConstructionSite constructionSite) {
		setConstructionSite(constructionSite);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 二维码保存路径.
	 */
	private String path;

	/**
	 * 二维码所贴在地点的经度.
	 */
	private String longitude;
	/**
	 * 二维码所贴在地点的纬度.
	 */
	private String latitude;

	/**
	 * 二维码黏贴在工地的具体位置.
	 */
	private String pasteAddress;

	/**
	 * 所属工地.
	 */
	@ManyToOne(optional = false)
	private ConstructionSite constructionSite;

	/**
	 * 二维码是否可用[是否激活]
	 */
	@Column(columnDefinition = "INT DEFAULT 0")
	private Boolean activated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getPasteAddress() {
		return pasteAddress;
	}

	public void setPasteAddress(String pasteAddress) {
		this.pasteAddress = pasteAddress;
	}

	public ConstructionSite getConstructionSite() {
		return constructionSite;
	}

	public void setConstructionSite(ConstructionSite constructionSite) {
		this.constructionSite = constructionSite;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

}
