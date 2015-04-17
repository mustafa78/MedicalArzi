/**
 * 
 */
package com.example.medicalarzi.dao;

import java.util.List;

import com.example.medicalarzi.model.Arzi;

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
	public List<Arzi> selectPatientsAllArzis(Long itsNumber);

}
