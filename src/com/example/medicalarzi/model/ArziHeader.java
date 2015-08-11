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
public class ArziHeader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9211679891809439814L;

	private Long arziId;

	private Long itsNumber;

	private Procedure procedure;

	private Condition condition;

	private ArziType arziType;

	private BodyPart bodyPart;

	private Status currentStatus;

	private GregHijDate requestSubmitDate = new GregHijDate();

	private GregHijDate currentStatusDate = new GregHijDate();

	private GregHijDate conditionStartDate = new GregHijDate();

	protected String createdBy;

	protected Date createdDate;

	protected String updatedBy;

	protected Date updatedDate;

	/**
	 * 
	 */
	public ArziHeader() {

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

	public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	public GregHijDate getRequestSubmitDate() {
		return requestSubmitDate;
	}

	public void setRequestSubmitDate(GregHijDate requestSubmitDate) {
		this.requestSubmitDate = requestSubmitDate;
	}

	public GregHijDate getCurrentStatusDate() {
		return currentStatusDate;
	}

	public void setCurrentStatusDate(GregHijDate currentStatusDate) {
		this.currentStatusDate = currentStatusDate;
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

	public GregHijDate getConditionStartDate() {
		return conditionStartDate;
	}

	public void setConditionStartDate(GregHijDate conditionStartDate) {
		this.conditionStartDate = conditionStartDate;
	}

	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}

}
