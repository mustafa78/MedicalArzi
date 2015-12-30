/**
 * 
 */
package com.example.medicalarzi.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author mkanchwa
 *
 */
public class ArziSearchCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7204569109820416459L;
	
	private Long itsNumber;
	
	private String firstName;
	
	private String lastName;
	
	private ArziType arziType;
	
	private BodyPart bodyPart;
	
	private Condition condition;
	
	private Procedure procedure;
	
	private Jamaat jamaat;
	
	private Lookup numberOfDays;
	
	private GregHijDate currentDate;
	
	private GregHijDate searchPeriodDate;

	/**
	 * 
	 */
	public ArziSearchCriteria() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getItsNumber() {
		return itsNumber;
	}

	public void setItsNumber(Long itsNumber) {
		this.itsNumber = itsNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Procedure getProcedure() {
		return procedure;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	public Jamaat getJamaat() {
		return jamaat;
	}

	public void setJamaat(Jamaat jamaat) {
		this.jamaat = jamaat;
	}

	public Lookup getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Lookup numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public GregHijDate getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(GregHijDate currentDate) {
		this.currentDate = currentDate;
	}

	public GregHijDate getSearchPeriodDate() {
		return searchPeriodDate;
	}

	public void setSearchPeriodDate(GregHijDate searchPeriodDate) {
		this.searchPeriodDate = searchPeriodDate;
	}

	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}	
}
