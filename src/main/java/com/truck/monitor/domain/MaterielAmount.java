package com.truck.monitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 物料每月总量.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class MaterielAmount {

	public MaterielAmount() {

	}

	public MaterielAmount(ConstructionSite site, Materiel materiel, int year, int month) {
		this.setMateriel(materiel);
		this.setSite(site);
		this.setYear(year);
		this.setMonth(month);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "year_")
	private int year;

	@Column(name = "month_")
	private int month;

	/**
	 * 量.
	 */
	private Float amount;

	@ManyToOne
	private Materiel materiel;

	@ManyToOne
	private ConstructionSite site;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Materiel getMateriel() {
		return materiel;
	}

	public void setMateriel(Materiel materiel) {
		this.materiel = materiel;
	}

	public ConstructionSite getSite() {
		return site;
	}

	public void setSite(ConstructionSite site) {
		this.site = site;
	}

}
