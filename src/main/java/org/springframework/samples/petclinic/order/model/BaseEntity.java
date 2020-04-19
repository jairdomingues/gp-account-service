package org.springframework.samples.petclinic.order.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	@Column(nullable = false)
	private Integer version;

	@Column(nullable = false, updatable = false)
	protected String createUser;

	@Column(nullable = false)
	private String changeUser;

	@Column(nullable = false, updatable = false)
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createDate;

	@Column(nullable = false)
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date changeDate;

	@Basic
	@Column(nullable = false, length = 40, updatable = false, insertable = true, unique = true)
	private String uuid;

	@PrePersist
	private void initTimeStamps() {
		if (this.createDate == null) {
			this.createDate = new Date();
		}
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
		}
		this.changeDate = this.createDate;
		this.createUser = "admin";
		this.changeUser = "admin";
	}

	@PreUpdate
	private void updateTimeStamp() {
		this.changeDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getChangeUser() {
		return changeUser;
	}

	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
