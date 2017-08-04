package com.truck.monitor.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 扫码.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class Scan {

	public Scan() {

	}

	public Scan(Long id, Date time, Long distance) {
		super();
		this.id = id;
		this.time = time;
		this.distance = distance;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 行为.
	 */
	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Behavior behavior;

	/**
	 * 所扫描的二维码.
	 */

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ConstructionSiteQrCode qrCode;

	/**
	 * 扫码时间.
	 */
	@Column(name = "time_")
	private Date time;

	/**
	 * 本次扫码距离二维码所贴位置的具体[厘米].
	 */
	private Long distance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

	public ConstructionSiteQrCode getQrCode() {
		return qrCode;
	}

	public void setQrCode(ConstructionSiteQrCode qrCode) {
		this.qrCode = qrCode;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

}
