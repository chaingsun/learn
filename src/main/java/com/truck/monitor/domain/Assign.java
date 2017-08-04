package com.truck.monitor.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class Assign {

	final public static String PATTERN = "yyyyMMdd";

	public Assign() {

	}

	public Assign(ConstructionSite site, Driver driver, Driver assigner, String day) {
		this.setSite(site);
		this.setDriver(driver);
		this.setAssigner(assigner);
		this.setDay(day);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 工地.
	 */
	@ManyToOne(optional = false)
	private ConstructionSite site;

	/**
	 * 司机.
	 */
	@ManyToOne(optional = false)
	private Driver driver;

	/**
	 * 指派人.
	 */
	@ManyToOne(optional = false)
	private Driver assigner;

	@Column(name = "day_")
	private String day;

	@Transient
	private Date begin;

	@Transient
	private Date end;

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConstructionSite getSite() {
		return site;
	}

	public void setSite(ConstructionSite site) {
		this.site = site;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Driver getAssigner() {
		return assigner;
	}

	public void setAssigner(Driver assigner) {
		this.assigner = assigner;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

}
