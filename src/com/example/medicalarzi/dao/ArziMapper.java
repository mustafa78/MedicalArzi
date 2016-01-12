/**
 * 
 */
package com.example.medicalarzi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziSearchCriteria;
import com.example.medicalarzi.model.ArziSearchResult;

/**
 * @author mkanchwa
 *
 */
public interface ArziMapper {

	/**
	 * This method is responsible for insert a new arzi for a patient.
	 * 
	 * @param newArzi
	 */
	public void insertPatientsNewArzi(Arzi newArzi);

	/**
	 * This method is responsible for returning all the saved or submitted arzis
	 * for a patient based on their ITS Number.
	 * 
	 * @param itsNumber
	 * @return java.util.List
	 */
	public List<Arzi> selectPatientsAllArzis(@Param("itsNumber") Long itsNumber);
	
	/**
	 * This method is responsible for returning the requested saved arzi for the
	 * patient based on their ITS number.
	 * 
	 * @param itsNumber
	 * @param arziId
	 * @return com.example.medicalarzi.model.Arzi
	 */
	public Arzi selectArziForPatient(@Param("itsNumber") Long itsNumber,
			@Param("arziId") Long arziId);
	
	/**
	 * This method is responsible for returning all the arzis for the patient
	 * based on their ITS number
	 * 
	 * @param itsNumber
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Arzi>
	 */
	public List<Arzi> selectAllArzisForPatient(
			@Param("itsNumber") Long itsNumber);
	
	/**
	 * This method is responsible for updating the saved arzis (in draft mode)
	 * by a patient before it is submitted.
	 * 
	 * @param editedArzi
	 */
	public void updateArziHdrSelective(Arzi editedArzi);
	
	/**
	 * This method is responsible for selecting the arzis based on the user
	 * entered search criteria.
	 * 
	 * @param searchCriteria
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.ArziSearchResult>
	 */
	public List<ArziSearchResult> selectArzisBySearchCriteria(ArziSearchCriteria searchCriteria);
	
	/**
	 * This method is responsible for selecting a particular arzi based on the
	 * the arzi id.
	 * 
	 * @param arziId
	 * 
	 * @return com.example.medicalarzi.model.Arzi
	 */
	public Arzi selectArziById(@Param("arziId") Long arziId);
	
	/**
	 * This method is responsible for deleting the Arzi record based on the arzi
	 * id from the header table.
	 * 
	 * @param arziId
	 */
	public void deleteArziHeaderById(@Param("arziId") Long arziId);
	
	/**
	 * This method is responsible for deleting the Arzi record based on the arzi
	 * id from the detail table.
	 * 
	 * @param arziId
	 */
	public void deleteArziDetailById(@Param("arziId") Long arziId);
	
	/**
	 * This method is responsible for updating the active indicator for the arzi
	 * to 'N'. It is a soft delete of the arzi instead of purging it from the
	 * database.
	 * 
	 * @param arziId
	 */
	public void updateArziHdrActiveInd(@Param("arziId") Long arziId);

}
