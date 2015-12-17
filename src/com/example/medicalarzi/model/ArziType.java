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
public class ArziType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1742880714078171023L;

	private Long arziTypeId;

	private String arziTypeName;

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

	public String getArziTypeName() {
		return arziTypeName;
	}

	public void setArziTypeName(String arziTypeName) {
		this.arziTypeName = arziTypeName;
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

	@Override
	public int hashCode() {
		return arziTypeId.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ArziType)) {
			return false;
		}
		ArziType other = (ArziType) obj;
		if (arziTypeId == null) {
			if (other.arziTypeId != null) {
				return false;
			}
		} else if (!arziTypeId.equals(other.arziTypeId)) {
			return false;
		}
		return true;
	}

}
