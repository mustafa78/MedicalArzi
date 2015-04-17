/**
 *
 */
package com.example.medicalarzi.dao;

import org.apache.ibatis.annotations.Param;

import com.example.medicalarzi.model.Patient;

/**
 * @author mkanchwa
 *
 */
public interface PatientMapper {

	/**
	 * This method is responsible for creating a new patient record in the
	 * database. Most probably when the patient goes through the registration
	 * process.
	 *
	 * @param ptnt
	 */
	public void insertPatientRecord(Patient ptnt);

	/**
	 * This method is responsible for retrieving the hashed password from the
	 * database and authenticate against the user entered password based on the
	 * ITS number.
	 *
	 * @param itsNumber
	 * @param password
	 * @param passString
	 *
	 * @return java.lang.String
	 */
	public String checkPatientPassword(@Param("itsNumber") Long itsNumber,
			@Param("password") String password,
			@Param("passString") String passString);

	/**
	 * This method is responsible for retrieving the Patient information from
	 * the database based on the ITS number.
	 *
	 * @param itsNumber
	 * @return
	 */
	public Patient retrievePatientInfo(@Param("itsNumber") Long itsNumber);

	/**
	 * This method is responsible for decoding the hashed password.
	 *
	 * @param itsNumber
	 * @param passString
	 *
	 * @return com.example.medicalarzi.model.Patient
	 */
	public String retrievePassword(@Param("itsNumber") Long itsNumber,
			@Param("passString") String passString);
}
