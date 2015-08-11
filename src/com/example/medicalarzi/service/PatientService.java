/**
 *
 */
package com.example.medicalarzi.service;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.Patient;

/**
 * @author mkanchwa
 *
 */
public interface PatientService {

	/**
	 * This method is responsible for registering the new patient.
	 *
	 * @param ptnt
	 */
	public void registerPatient(Patient ptnt);

	/**
	 * This method is responsible for authenticating the already registered
	 * patient into the system based on his credentials
	 *
	 * @param itsNumber
	 * @param password
	 * @param passString
	 *
	 * @return java.lang.String
	 */
	public String authenticate(Long itsNumber, String password,
			String passString);

	/**
	 * This method is responsible for getting the patient information based on
	 * the itsNumber. If the patient is already registered, his/her information
	 * should be retrieved by this call and the flag is set accordingly.
	 *
	 * @param itsNumber
	 *
	 * @return boolean
	 */
	public boolean checkIfPtntAlreadyRegistered(Long itsNumber);

	/**
	 * This method is responsible for getting the patient details based on the
	 * ITS number. The entire patient infromation should be available through
	 * this method.
	 *
	 * @param itsNumber
	 *
	 * @return com.example.medicalarzi.model.Patient
	 */
	public Patient getPatientInfo(Long itsNumber);

	/**
	 * This method is responsible for retrieving the password for the patient
	 * from the database and then email the retrieved password to the user.
	 *
	 * @param itsNumber
	 * @param passString
	 * @return
	 */
	public String emailForgotPassword(Long itsNumber, String passString);
	
	/**
	 * This method is responsible for updating the patient information.
	 * 
	 * @param ptnt
	 */
	public void updatePatientInfo(Patient ptnt);
	
	/**
	 * This method is responsible for creating a new arzi for the patient based
	 * on the patient's ITS number.
	 * 
	 * @param newArzi
	 */
	public void createNewArzi(Arzi newArzi);

}
