package com.truck.monitor.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 主管.
 * 
 * @author lzq
 *
 */
@Entity
@Table
@JsonInclude(Include.NON_NULL)
public class Director extends User {

	public Director() {

	}

	public Director(Long id) {
		this.setId(id);
	}

	public Director(Long id, String phone, String name) {
		super(id, phone, name);
	}

}
