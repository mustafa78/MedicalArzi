package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;

public class Arzi implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3071236194000826029L;

	private Long arziId;

	private Long itsNumber;
	
	private Long drItsNumber;

	private Procedure procedure;

	private Condition condition;

	private Status status;

	private ArziType arziType;
	
	private BodyPart bodyPart;
	
	private Date arziSubmitDate;
	
	private Date drReviewDate;

	private String createdBy;

	private Date createdDate;

	private String updatedBy;

	private Date updatedDate;

	public Arzi() {
		// TODO Auto-generated constructor stub
	}

	public Long getArziId() {
		return arziId;
	}
	
	public void setArziId(Long arziId) {
		this.arziId = arziId;
	}

	public Long getItsNumber() {
		return itsNumber;
	}

	public void setItsNumber(Long itsNumber) {
		this.itsNumber = itsNumber;
	}

	public Procedure getProcedure() {
		return procedure;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getDrItsNumber() {
		return drItsNumber;
	}

	public void setDrItsNumber(Long drItsNumber) {
		this.drItsNumber = drItsNumber;
	}

	public ArziType getArziType() {
		return arziType;
	}

	public void setArziType(ArziType arziType) {
		this.arziType = arziType;
	}

	public BodyPart getBodyPart() {
		return bodyPart;
	}

	public void setBodyPart(BodyPart bodyPart) {
		this.bodyPart = bodyPart;
	}

	public Date getArziSubmitDate() {
		return arziSubmitDate;
	}

	public void setArziSubmitDate(Date arziSubmitDate) {
		this.arziSubmitDate = arziSubmitDate;
	}

	public Date getDrReviewDate() {
		return drReviewDate;
	}

	public void setDrReviewDate(Date drReviewDate) {
		this.drReviewDate = drReviewDate;
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
