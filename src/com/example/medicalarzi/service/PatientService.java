/**
 *
 */
package com.example.medicalarzi.service;

import java.util.List;

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
	 * @param com.example.medicalarzi.model.Patient
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
	 * 
	 * @return java.lang.String
	 */
	public String emailForgotPassword(Long itsNumber, String passString);
	
	/**
	 * This method is responsible for updating the patient information.
	 * 
	 * @param com.example.medicalarzi.model.Patient
	 */
	public void updatePatientInfo(Patient ptnt);
	
	/**
	 * This method is responsible for creating a new arzi for the patient based
	 * on the patient's ITS number.
	 * 
	 * @param com.example.medicalarzi.model.Arzi
	 */
	public void createNewArzi(Arzi newArzi);
	
	/**
	 * This method is responsible for retrieving all the saved/submitted arzis
	 * for a patient based on the patient's ITS number.
	 * 
	 * @param itsNumber
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Arzi>
	 */
	public List<Arzi> retrieveAllArzisForPatient(Long itsNumber);
	
	/**
	 * This method is responsible for retrieving a specific arzi for a patient
	 * based on the patient's ITS number and the arzi Id.
	 * 
	 * @param itsNumber
	 * @param arziId
	 * 
	 * @return com.example.medicalarzi.model.Arzi
	 */
	public Arzi retrieveArziForPatient(Long itsNumber, Long arziId);
	
	/**
	 * This method is responsible for updating an existing arzi in the draft
	 * mode before it is submitted. The patient can make the updates before
	 * submitting if the information was not correct before it was saved.
	 * 
	 * @param editedArzi
	 */
	public void updateAnExistingArziInDraftMode(Arzi editedArzi);
	
	/**
	 * This method is responsible for the soft delete of an arzi instead of
	 * purging it from the database. It just toggles the active indicator for
	 * the arzi to 'N' when the user tries to delete an arzi in the 'Draft'
	 * status
	 * 
	 * @param arziId
	 */
	public void deactivateAnArziById(Long arziId);

}
