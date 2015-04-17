package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mkanchwa
 *
 */
public class SecurityRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1874607820319722660L;

	private Long secRoleId;

	private String secRoleName;

	private String moduleName;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	public SecurityRole() {
		// TODO Auto-generated constructor stub
	}

	public Long getSecRoleId() {
		return secRoleId;
	}

	public void setSecRoleId(Long secRoleId) {
		this.secRoleId = secRoleId;
	}

	public String getSecRoleName() {
		return secRoleName;
	}

	public void setSecRoleName(String secRoleName) {
		this.secRoleName = secRoleName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
