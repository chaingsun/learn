/*
 * Copyright 2015-2020 reserved by jf61.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.truck.monitor.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 运输.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class Transport {

	public Transport() {

	}

	public Transport(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 运货类型.
	 */
	@ManyToOne
	private Materiel materiel;
	/**
	 * 是否满载.
	 */
	@Column(columnDefinition = "INT DEFAULT 0")
	private Boolean fully;
	/**
	 * 司机.
	 */
	@ManyToOne(optional = false)
	private Driver driver;

	/**
	 * 价格[分].
	 */
	private Long price;

	/**
	 * 司机酬劳[分].
	 */
	private Long reward;

	/**
	 * 工地.
	 */
	@ManyToOne(optional = false)
	private ConstructionSite constructionSite;

	/**
	 * 时间.
	 */
	@Column(name = "time_", nullable = false)
	private Date time;

	/**
	 * 运输号.
	 */
	@Column(name = "_code", nullable = false)
	private String code;

	/**
	 * 
	 */
	@Column(name = "year_", nullable = false)
	private Integer year;

	@Column(name = "month_", nullable = false)
	private Integer month;

	@Column(name = "day_", nullable = false)
	private Integer day;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Behavior.Type status;

	@ManyToOne
	private Motorcade motorcade;

	/**
	 * 审核员.
	 */
	@ManyToOne
	private Director auditor;

	/**
	 * 审核时间.
	 */
	private Date auditTime;

	/**
	 * 备注.
	 */
	private String comments;

	/**
	 * 泥尾.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private Destination destination;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Materiel getMateriel() {
		return materiel;
	}

	public void setMateriel(Materiel materiel) {
		this.materiel = materiel;
	}

	public Boolean getFully() {
		return fully;
	}

	public void setFully(Boolean fully) {
		this.fully = fully;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public ConstructionSite getConstructionSite() {
		return constructionSite;
	}

	public void setConstructionSite(ConstructionSite constructionSite) {
		this.constructionSite = constructionSite;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Behavior.Type getStatus() {
		return status;
	}

	public void setStatus(Behavior.Type status) {
		this.status = status;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Director getAuditor() {
		return auditor;
	}

	public void setAuditor(Director auditor) {
		this.auditor = auditor;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Motorcade getMotorcade() {
		return motorcade;
	}

	public void setMotorcade(Motorcade motorcade) {
		this.motorcade = motorcade;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public Long getReward() {
		return reward;
	}

	public void setReward(Long reward) {
		this.reward = reward;
	}

}
