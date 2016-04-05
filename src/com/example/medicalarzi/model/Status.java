/**
 *
 */
package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author mkanchwa
 *
 */
public class Status implements Serializable {

	private static final long serialVersionUID = -6327353038180403874L;
	
	private Long statusId;

	private String statusDesc;
	
	private String statusFrom;
	
	private String statusTo;
	
	private String statusLongDesc;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	/**
	 *
	 */
	public Status() {

	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getStatusFrom() {
		return statusFrom;
	}

	public void setStatusFrom(String statusFrom) {
		this.statusFrom = statusFrom;
	}

	public String getStatusTo() {
		return statusTo;
	}

	public void setStatusTo(String statusTo) {
		this.statusTo = statusTo;
	}

	public String getStatusLongDesc() {
		return statusLongDesc;
	}

	public void setStatusLongDesc(String statusLongDesc) {
		this.statusLongDesc = statusLongDesc;
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

	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}
}
