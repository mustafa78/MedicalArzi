/**
 * 
 */
package com.example.medicalarzi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.medicalarzi.model.Arzi;

/**
 * @author mkanchwa
 *
 */
public interface ArziMapper {

	/**
	 * This method is responsible for insert a new arzi for a patient.
	 * 
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
	public Arzi selectArziForPatient(@Param("itsNumber") Long itsNumber, @Param("arziId") Long arziId);

}
