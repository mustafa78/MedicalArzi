/**
 *
 */
package com.example.medicalarzi.model;

import java.io.Serializable;

/**
 * @author mkanchwa
 *
 */
public class Speciality implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1961333152781683893L;

	private Long specialityId;
	
	private String specialityGroup;

	private String specialityName;

	/**
	 *
	 */
	public Speciality() {
		// TODO Auto-generated constructor stub
	}

	public Long getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Long specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityGroup() {
		return specialityGroup;
	}

	public void setSpecialityGroup(String specialityGroup) {
		this.specialityGroup = specialityGroup;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

}
