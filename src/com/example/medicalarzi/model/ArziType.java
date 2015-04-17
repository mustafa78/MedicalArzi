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
public class ArziType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1742880714078171023L;
	
	private Long arziTypeId;
	
	private String arzTypeName;
	
	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	/**
	 * 
	 */
	public ArziType() {
		// TODO Auto-generated constructor stub
	}

	public Long getArziTypeId() {
		return arziTypeId;
	}

	public void setArziTypeId(Long arziTypeId) {
		this.arziTypeId = arziTypeId;
	}

	public String getArzTypeName() {
		return arzTypeName;
	}

	public void setArzTypeName(String arzTypeName) {
		this.arzTypeName = arzTypeName;
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
