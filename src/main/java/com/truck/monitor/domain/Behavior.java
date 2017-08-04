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
import javax.persistence.Transient;

/**
 * 运输行为[进场/卸货/出场.].
 * 
 * @author lzq
 *
 */
@Table
@Entity
public class Behavior {

	public enum Type {

		/**
		 * 进场.
		 */
		IN("进场"),

		/**
		 * 离场.
		 */
		OUT("离场"),
		/**
		 * 卸货.
		 */
		DISCHARGE(" 卸货");

		private String name;

		private Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public Behavior() {

	}

	public Behavior(Director auditor) {
		this.setAuditor(auditor);
	}

	public Behavior(Long id, Date time, String longitude, String latitude, Type type) {
		super();
		this.id = id;
		this.time = time;
		this.longitude = longitude;
		this.latitude = latitude;
		this.type = type;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Transport transport;

	/**
	 * 发生所在时间.
	 */
	@Column(name = "time_", nullable = false)
	private Date time;

	/**
	 * 发生行为所在的经度位置.
	 */
	@Column(nullable = false)
	private String longitude;
	/**
	 * 发生该行为所在的纬度位置.
	 */
	@Column(nullable = false)
	private String latitude;

	/**
	 * 行为类型.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "type_", nullable = false)
	public Behavior.Type type;

	/**
	 * 审核员[多余属性].
	 */
	@Transient
	private Director auditor;

	/**
	 * 审核时间[多余属性].
	 */
	@Transient
	private Date auditTime;

	/**
	 * 备注[多余属性].
	 */
	@Transient
	private String comments;

	@Transient
	private Scan scan;

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Behavior.Type getType() {
		return type;
	}

	public void setType(Behavior.Type type) {
		this.type = type;
	}

	public Director getAuditor() {
		return auditor;
	}

	public void setAuditor(Director auditor) {
		this.auditor = auditor;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Scan getScan() {
		return scan;
	}

	public void setScan(Scan scan) {
		this.scan = scan;
	}

}
