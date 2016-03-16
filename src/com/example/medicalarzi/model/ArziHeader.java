/**
 * 
 */
package com.example.medicalarzi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

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

	@NotNull(message = "Please enter the ITS number.")
	private Long itsNumber;

	private Procedure procedure;

	private Condition condition;

	private ArziType arziType;

	private BodyPart bodyPart;

	private Status currentStatus;
	
	private String otherCondition;
	
	private String otherProcedure;
	
	private String otherBodyPart;

	// These properties are initialized because when we are binding the
	// properties to the UI widgets, the nested properties throw exceptions if
	// the intermediate properties are null.
	private GregHijDate requestSubmitDate = new GregHijDate();

	private GregHijDate currentStatusDate = new GregHijDate();

	private GregHijDate conditionStartDate = new GregHijDate();
	
	private String arziSummary;
	
	private String doctorConsultNote;
	
	/*
	 * If the location at the time of submitting the arzi is not the patient's
	 * primary location, all this information will be stored along with the arzi
	 * and the primary location info in the Patient's table will not be updated.
	 */
	private Boolean primaryAddrInd;
	
	private String ptntHomeAddress1;

	private String ptntHomeAddress2;
	
	private String ptntPhoneNum;
	
	private String ptntZip;
	
	private Location ptntLocation;
	
	private List<ArziComment> arziComments;
	
	private Boolean activeInd;

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

	public String getOtherCondition() {
		return otherCondition;
	}

	public void setOtherCondition(String otherCondition) {
		this.otherCondition = otherCondition;
	}
	
	public String getOtherProcedure() {
		return otherProcedure;
	}

	public void setOtherProcedure(String otherProcedure) {
		this.otherProcedure = otherProcedure;
	}

	public String getOtherBodyPart() {
		return otherBodyPart;
	}

	public void setOtherBodyPart(String otherBodyPart) {
		this.otherBodyPart = otherBodyPart;
	}

	public Boolean getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(Boolean activeInd) {
		this.activeInd = activeInd;
	}

	public String getArziSummary() {
		return arziSummary;
	}

	public void setArziSummary(String arziSummary) {
		this.arziSummary = arziSummary;
	}

	public String getDoctorConsultNote() {
		return doctorConsultNote;
	}

	public void setDoctorConsultNote(String doctorConsultNote) {
		this.doctorConsultNote = doctorConsultNote;
	}

	public Boolean getPrimaryAddrInd() {
		return primaryAddrInd;
	}

	public void setPrimaryAddrInd(Boolean primaryAddrInd) {
		this.primaryAddrInd = primaryAddrInd;
	}

	public String getPtntHomeAddress1() {
		return ptntHomeAddress1;
	}

	public void setPtntHomeAddress1(String ptntHomeAddress1) {
		this.ptntHomeAddress1 = ptntHomeAddress1;
	}

	public String getPtntHomeAddress2() {
		return ptntHomeAddress2;
	}

	public void setPtntHomeAddress2(String ptntHomeAddress2) {
		this.ptntHomeAddress2 = ptntHomeAddress2;
	}

	public String getPtntPhoneNum() {
		return ptntPhoneNum;
	}

	public void setPtntPhoneNum(String ptntPhoneNum) {
		this.ptntPhoneNum = ptntPhoneNum;
	}

	public Location getPtntLocation() {
		return ptntLocation;
	}

	public void setPtntLocation(Location ptntLocation) {
		this.ptntLocation = ptntLocation;
	}

	public String getPtntZip() {
		return ptntZip;
	}

	public void setPtntZip(String ptntZip) {
		this.ptntZip = ptntZip;
	}

	public List<ArziComment> getArziComments() {
		return arziComments;
	}

	public void setArziComments(List<ArziComment> arziComments) {
		this.arziComments = arziComments;
	}

	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}	

}
