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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonInclude(Include.NON_NULL)
public class User {

	public enum Type {
		OPS, DRIVER, DIRECTOR
	}

	public User() {

	}

	public User(Long id, String phone, String name) {
		this.setId(id);
		this.setPhone(phone);
		this.setName(name);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 手机号码.
	 */
	private String phone;
	/**
	 * 真实姓名.
	 */
	private String name;

	/**
	 * 注册时间.
	 */
	@Column(name = "reg_time")
	private Date regTime;
	/**
	 * 用户状态.
	 */
	@Column(name = "status_", columnDefinition = "INT DEFAULT 0")
	private Integer status;

	/**
	 * 编号.
	 */
	@Column(name = "code_")
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "openid")
	private Follower follower;

	/**
	 * 搜索的关键字.
	 */
	@Transient
	private String keyWord;

	@Transient
	public Type getType() {
		Class<?> clazz = this.getClass();
		if (clazz.equals(Director.class)) {
			return Type.DIRECTOR;
		} else if (clazz.equals(Driver.class)) {
			return Type.DRIVER;
		} else if (clazz.equals(OpsUser.class)) {
			return Type.OPS;
		}
		return null;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Follower getFollower() {
		return follower;
	}

	public void setFollower(Follower follower) {
		this.follower = follower;
	}

}
