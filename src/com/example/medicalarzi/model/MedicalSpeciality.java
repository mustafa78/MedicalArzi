/**
 *
 */
package com.example.medicalarzi.model;

import java.io.Serializable;

/**
 * @author mkanchwa
 *
 */
public class MedicalSpeciality implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1961333152781683893L;

	private Long medSpecialityId;

	private String medSpecialityName;

	/**
	 *
	 */
	public MedicalSpeciality() {
		// TODO Auto-generated constructor stub
	}

	public Long getMedSpecialityId() {
		return medSpecialityId;
	}

	public void setMedSpecialityId(Long medSpecialityId) {
		this.medSpecialityId = medSpecialityId;
	}

	public String getMedSpecialityName() {
		return medSpecialityName;
	}

	public void setMedSpecialityName(String medSpecialityName) {
		this.medSpecialityName = medSpecialityName;
	}

}
