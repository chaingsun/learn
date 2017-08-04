package com.truck.monitor.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 运输酬劳.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class Reward {

	public Reward() {
	}

	public Reward(Long id, Materiel materiel, Long reward) {
		this.id = id;
		this.materiel = materiel;
		this.reward = reward;
	}
	

	public Reward(ConstructionSite site, Destination destination, Materiel materiel) {
		super();
		this.site = site;
		this.destination = destination;
		this.materiel = materiel;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	private ConstructionSite site;

	@ManyToOne(optional = false)
	private Destination destination;

	@ManyToOne(optional = false)
	private Materiel materiel;

	private Long reward;

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

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public Materiel getMateriel() {
		return materiel;
	}

	public void setMateriel(Materiel materiel) {
		this.materiel = materiel;
	}

	public Long getReward() {
		return reward;
	}

	public void setReward(Long reward) {
		this.reward = reward;
	}

}
