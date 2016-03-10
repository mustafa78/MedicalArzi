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
public class MedicalHistory implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7064376663385866662L;

	private Long itsNumber;

	private String primaryDrName;

	private String primaryDrPhone;

	private Boolean asthmaInd;

	private Boolean cholesterolInd;

	private Boolean atrialFibrillationInd;

	private Boolean diabetesInd;
	
	private Boolean diabetesType;

	private Boolean hyperTensionInd;

	private Boolean thyroidDisorderInd;

	private Boolean heartDiseaseInd;

	private String heartDiseaseType;

	private Boolean cancerInd;

	private String cancerType;

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

	public Long getItsNumber() {
		return itsNumber;
	}

	public void setItsNumber(Long itsNumber) {
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

	public Boolean getAsthmaInd() {
		return asthmaInd;
	}

	public void setAsthmaInd(Boolean asthmaInd) {
		this.asthmaInd = asthmaInd;
	}

	public Boolean getCholesterolInd() {
		return cholesterolInd;
	}

	public void setCholesterolInd(Boolean cholesterolInd) {
		this.cholesterolInd = cholesterolInd;
	}

	public Boolean getAtrialFibrillationInd() {
		return atrialFibrillationInd;
	}

	public void setAtrialFibrillationInd(Boolean atrialFibrillationInd) {
		this.atrialFibrillationInd = atrialFibrillationInd;
	}

	public Boolean getDiabetesInd() {
		return diabetesInd;
	}

	public void setDiabetesInd(Boolean diabetesInd) {
		this.diabetesInd = diabetesInd;
	}

	public Boolean getHyperTensionInd() {
		return hyperTensionInd;
	}

	public Boolean getDiabetesType() {
		return diabetesType;
	}

	public void setDiabetesType(Boolean diabetesType) {
		this.diabetesType = diabetesType;
	}

	public void setHyperTensionInd(Boolean hyperTensionInd) {
		this.hyperTensionInd = hyperTensionInd;
	}

	public Boolean getThyroidDisorderInd() {
		return thyroidDisorderInd;
	}

	public void setThyroidDisorderInd(Boolean thyroidDisorderInd) {
		this.thyroidDisorderInd = thyroidDisorderInd;
	}

	public Boolean getHeartDiseaseInd() {
		return heartDiseaseInd;
	}

	public void setHeartDiseaseInd(Boolean heartDiseaseInd) {
		this.heartDiseaseInd = heartDiseaseInd;
	}

	public String getHeartDiseaseType() {
		return heartDiseaseType;
	}

	public void setHeartDiseaseType(String heartDiseaseType) {
		this.heartDiseaseType = heartDiseaseType;
	}

	public Boolean getCancerInd() {
		return cancerInd;
	}

	public void setCancerInd(Boolean cancerInd) {
		this.cancerInd = cancerInd;
	}

	public String getCancerType() {
		return cancerType;
	}

	public void setCancerType(String cancerType) {
		this.cancerType = cancerType;
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

	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}
}
