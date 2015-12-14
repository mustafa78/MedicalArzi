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
public class Jamaat implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long jamaatId;

	private String jamaatName;
	
	private String jamiyatName;

	private String region;

	private String country;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	/**
	 * 
	 */
	public Jamaat() {
	}

	public Long getJamaatId() {
		return jamaatId;
	}

	public void setJamaatId(Long jamaatId) {
		this.jamaatId = jamaatId;
	}

	public String getJamaatName() {
		return jamaatName;
	}

	public void setJamaatName(String jamaatName) {
		this.jamaatName = jamaatName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getJamiyatName() {
		return jamiyatName;
	}

	public void setJamiyatName(String jamiyatName) {
		this.jamiyatName = jamiyatName;
	}
	
	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}

	@Override
	public int hashCode() {
        return jamaatId.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Jamaat)) {
			return false;
		}
		Jamaat other = (Jamaat) obj;
		if (jamaatId == null) {
			if (other.jamaatId != null) {
				return false;
			}
		} else if (!jamaatId.equals(other.jamaatId)) {
			return false;
		}
		return true;
	}

}
