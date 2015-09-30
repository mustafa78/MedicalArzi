/**
 *
 */
package com.example.medicalarzi.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.example.medicalarzi.dao.ArziMapper;
import com.example.medicalarzi.dao.PatientMapper;
import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.service.PatientService;

/**
 * @author mkanchwa
 *
 */
public class PatientServiceImpl implements PatientService {

	public static Logger logger = (Logger) LogManager
			.getLogger(PatientServiceImpl.class);

	private PatientMapper ptntMapper;
	
	private ArziMapper arziMapper;

	/**
	 *
	 */
	public PatientServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public PatientMapper getPtntMapper() {
		return ptntMapper;
	}

	public void setPtntMapper(PatientMapper ptntMapper) {
		this.ptntMapper = ptntMapper;
	}

	
	public ArziMapper getArziMapper() {
		return arziMapper;
	}

	public void setArziMapper(ArziMapper arziMapper) {
		this.arziMapper = arziMapper;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.example.medicalarzi.service.PatientService#registerPatient(com.example
	 * .medicalarzi.model.Patient)
	 */
	@Override
	public void registerPatient(Patient ptnt) {
		logger.debug("Registering the patient with ITS Number: "
				+ ptnt.getItsNumber());

		ptntMapper.insertPatientRecord(ptnt);

		logger.debug("Patient -> " + ptnt.getFirstName() + " "
				+ ptnt.getLastName() + " successfully registered");
	}

	@Override
	public String authenticate(Long itsNumber, String password,
			String passString) {
		logger.debug("Authenticating the patient with ITS Number: " + itsNumber);

		String result = ptntMapper.checkPatientPassword(itsNumber, password,
				passString);

		return result;
	}

	@Override
	public boolean checkIfPtntAlreadyRegistered(Long itsNumber) {
		logger.debug("Fetching patient with ITS Number: " + itsNumber);

		Patient ptntInfo = ptntMapper.retrievePatientInfo(itsNumber);

		if (ptntInfo != null) {
			logger.debug("The patient, "
					+ ptntInfo.getFirstName()
					+ " "
					+ ptntInfo.getLastName()
					+ " information is successfull retrieved. The patient is already registered.");

			return true;
		}

		return false;
	}

	@Override
	public Patient getPatientInfo(Long itsNumber) {
		logger.debug("Fetching patient with ITS Number: ->" + itsNumber);

		Patient ptntInfo = ptntMapper.retrievePatientInfo(itsNumber);

		return ptntInfo;
	}

	@Override
	public String emailForgotPassword(Long itsNumber, String passString) {
		logger.debug("Fetching password for the patient with ITS Number: ->"
				+ itsNumber);

		String password = ptntMapper.retrievePassword(itsNumber, passString);

		logger.debug("The retrieved password for the patient with ITS Number: "
				+ itsNumber + " is => " + password);

		return password;
	}

	@Override
	public void updatePatientInfo(Patient ptnt) {
		logger.debug("Updating the information for patient with ITS Number: -> "
				+ ptnt.getItsNumber());

		ptntMapper.updatePatientSelective(ptnt);

		logger.debug("Patient information for -> " + ptnt.getFirstName() + " "
				+ ptnt.getLastName() + " is successfully updated");
	}

	@Override
	public void createNewArzi(Arzi newArzi) {
		logger.debug("Registering the patient with ITS number: -> "
				+ newArzi.getItsNumber());

		arziMapper.insertPatientsNewArzi(newArzi);

		logger.debug("Arzi for patient with ITS number -> "
				+ newArzi.getItsNumber() + " successfully registered");
	}

	@Override
	public List<Arzi> retrieveAllArzisForPatient(Long itsNumber) {
		logger.debug("Retreiving all the arzis submitted by the patient with ITS number: -> "
				+ itsNumber);

		List<Arzi> arziList = arziMapper.selectAllArzisForPatient(itsNumber);

		return arziList;
	}

	@Override
	public Arzi retrieveArziForPatient(Long itsNumber, Long arziId) {
		logger.debug("Retreiving arzi with \"" + arziId + "\" "
				+ "submitted by the patient with ITS number: -> " + itsNumber);

		Arzi arzi = arziMapper.selectArziForPatient(itsNumber, arziId);

		return arzi;
	}

	@Override
	public void updateAnExistingArziInDraftMode(Arzi editedArzi) {
		logger.debug("Updating arzi with \"" + editedArzi.getArziId() + "\" "
				+ "by the patient with ITS number: -> "
				+ editedArzi.getItsNumber());

		arziMapper.updateArziHdrSelective(editedArzi);

		logger.debug("Arzi with \"" + editedArzi.getArziId()
				+ "\" successfully updated");

	}

}
