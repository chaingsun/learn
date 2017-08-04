package com.truck.monitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 车队.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class Motorcade {

	public Motorcade() {

	}

	public Motorcade(Long id) {
		this.setId(id);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	/**
	 * 是否需要分配旗下的司机到指定的工地.
	 */
	@Column(columnDefinition = "INT DEFAULT 0")
	private Boolean assign;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getAssign() {
		return assign;
	}

	public void setAssign(Boolean assign) {
		this.assign = assign;
	}

}
