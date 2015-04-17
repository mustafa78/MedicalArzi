/**
 *
 */
package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mkanchwa
 *
 */
public class PatientSecurityRole implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3854272705590803002L;

	private String ptntItsNumber;

	private String ptntSecRoleId;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;


	/**
	 *
	 */
	public PatientSecurityRole() {
		// TODO Auto-generated constructor stub
	}


	public String getPtntItsNumber() {
		return ptntItsNumber;
	}


	public void setPtntItsNumber(String ptntItsNumber) {
		this.ptntItsNumber = ptntItsNumber;
	}


	public String getPtntSecRoleId() {
		return ptntSecRoleId;
	}


	public void setPtntSecRoleId(String ptntSecRoleId) {
		this.ptntSecRoleId = ptntSecRoleId;
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
