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
public class MedicalHistory implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7064376663385866662L;

	private Long medHistId;

	private String itsNumber;

	private String primaryDrName;

	private String primaryDrPhone;

	private String otherInfo;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	/**
	 *
	 */
	public MedicalHistory() {
		// TODO Auto-generated constructor stub
	}

	public Long getMedHistId() {
		return medHistId;
	}

	public void setMedHistId(Long medHistId) {
		this.medHistId = medHistId;
	}

	public String getItsNumber() {
		return itsNumber;
	}

	public void setItsNumber(String itsNumber) {
		this.itsNumber = itsNumber;
	}

	public String getPrimaryDrName() {
		return primaryDrName;
	}

	public void setPrimaryDrName(String primaryDrName) {
		this.primaryDrName = primaryDrName;
	}

	public String getPrimaryDrPhone() {
		return primaryDrPhone;
	}

	public void setPrimaryDrPhone(String primaryDrPhone) {
		this.primaryDrPhone = primaryDrPhone;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
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
