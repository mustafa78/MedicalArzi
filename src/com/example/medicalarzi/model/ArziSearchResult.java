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
public class ArziSearchResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9144812361774229807L;

	private Arzi arzi;
	
	private Patient patient;

	/**
	 * 
	 */
	public ArziSearchResult() {
		
	}
	
	public Arzi getArzi() {
		return arzi;
	}

	public void setArzi(Arzi arzi) {
		this.arzi = arzi;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (new ReflectionToStringBuilder(this,
				RecursiveToStringStyle.MULTI_LINE_STYLE)).toString();
	}	

}
