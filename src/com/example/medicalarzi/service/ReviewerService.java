/**
 * 
 */
package com.example.medicalarzi.service;

import java.util.List;

import com.example.medicalarzi.model.ArziSearchCriteria;
import com.example.medicalarzi.model.ArziSearchResult;

/**
 * This purpose of this service is to help the Doctors perform various actions
 * during the lifecycle of the MAP application
 * 
 * @author mkanchwa
 *
 */
public interface ReviewerService {
	
	/**
	 * This method is responsible for returning the patient submitted arzis for review
	 * based on the selected criteria.
	 * 
	 * @param criteria
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.ArziSearchResult>
	 */
	public List<ArziSearchResult> searchArzisByCriteria(ArziSearchCriteria criteria);
	

}
