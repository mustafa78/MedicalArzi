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
import com.example.medicalarzi.model.MedicalHistory;
import com.example.medicalarzi.model.Patient;
import com.example.medicalarzi.service.PatientService;
import com.example.medicalarzi.util.MedicalArziConstants;

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
		
		if (newArzi.getCurrentStatus().getStatusId()
				.equals(MedicalArziConstants.ARZI_SUBMITTED_STATUS)) {

			arziMapper.insertArziDetail(newArzi);
		}

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
	public void updateAnExistingArzi(Arzi editedArzi) {
		logger.debug("Updating arzi with \"" + editedArzi.getArziId() + "\" "
				+ "by the patient with ITS number: -> "
				+ editedArzi.getItsNumber());

		arziMapper.updateArziHdrSelective(editedArzi);
		
		if (!editedArzi.getCurrentStatus().getStatusId()
				.equals(MedicalArziConstants.ARZI_DRAFT_STATUS)) {

			arziMapper.insertArziDetail(editedArzi);
		}

		logger.debug("Arzi with \"" + editedArzi.getArziId()
				+ "\" successfully updated");
	}

	@Override
	public void deactivateAnArziById(Long arziId) {
		logger.debug("Deleting/Deactivating arzi with \"" + arziId + "\" ");

		arziMapper.updateArziHdrActiveInd(arziId);

		logger.debug("Arzi with \"" + arziId + "\" successfully deactivated");
	}

	@Override
	public MedicalHistory retreivePatientsMedicalHistory(Long itsNumber) {
		
		logger.debug("Retreiving medical history for patient with ITS number: \""
				+ itsNumber + "\" ");
		
		MedicalHistory medHist = ptntMapper.retrievePtntMedicalHistory(itsNumber);
		
		return medHist;
	}

	@Override
	public void savePatientsMedicalHistory(MedicalHistory medicalHistory) {
		logger.debug("Saving medical history for patient with ITS number: \""
				+ medicalHistory.getItsNumber() + "\" ");
		
		ptntMapper.insertPtntMedicalHistory(medicalHistory);
		
		logger.debug("Medical history successfully saved.");
	}

	@Override
	public void updatePatientsMedicalHistory(MedicalHistory medicalHistory) {
		logger.debug("Updating medical history for patient with ITS number: \""
				+ medicalHistory.getItsNumber() + "\" ");
		
		ptntMapper.updatePtntMedicalHistory(medicalHistory);
		
		logger.debug("Medical history successfully updated.");
	}
}
