package com.truck.monitor.domain;

import javax.persistence.*;

/**
 * 司机.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class Driver extends User {

	public Driver() {

	}

	public Driver(Long id, String phone, String name) {
		super(id, phone, name);
	}

	public Driver(Long id, String phone, String name, String licensePlate, String carNo) {
		super(id, phone, name);
		this.setLicensePlate(licensePlate);
		this.setCarNo(carNo);
	}

	public Driver(Long id, String phone, String name, String licensePlate, String carNo, String siteName) {
		super(id, phone, name);
		this.setLicensePlate(licensePlate);
		this.setCarNo(carNo);
		this.setSiteName(siteName);
	}

	public Driver(Long id) {
		this.setId(id);
	}

	/**
	 * 所在车队.
	 */
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, optional = false)
	private Motorcade	     motorcade;

	/**
	 * 是否为队长.
	 */
	@Column(columnDefinition = "INT DEFAULT 0")
	private Boolean	         captain;

	/**
	 * 车牌.
	 */
	private String	         licensePlate;

	/**
	 * 车号.
	 */
	private String	         carNo;

	/*
	 * 工地名称
	 */
	private transient String	siteName;

	public Motorcade getMotorcade() {
		return motorcade;
	}

	public void setMotorcade(Motorcade motorcade) {
		this.motorcade = motorcade;
	}

	public Boolean getCaptain() {
		return captain;
	}

	public void setCaptain(Boolean captain) {
		this.captain = captain;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
}
