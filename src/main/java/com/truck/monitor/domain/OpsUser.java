package com.truck.monitor.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统管理员.
 * 
 * @author lzq
 *
 */
@Entity
@Table
public class OpsUser extends User {

	/**
	 * 密码.
	 */
	private String pwd;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
