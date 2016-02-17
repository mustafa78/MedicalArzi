/**
 * 
 */
package com.example.medicalarzi.service;

import java.util.List;

import com.example.medicalarzi.model.Arzi;
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
	
	/**
	 * This method is responsible for fetching/retrieving all the arzis assigned
	 * to the reviewer for review. The reviewers are suppose to work only on
	 * arzis that are assigned to them.
	 * 
	 * @param reviewerItsNumber
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.ArziSearchResult>
	 */
	public List<ArziSearchResult> retrieveArzisAssignedToReviewer(Long reviewerItsNumber);
	
	/**
	 * This method is responsible for updating the arzi status to
	 * "Work in Progress" when the reviewer (doctor) self assigns the arzi to
	 * himself. Also this method creates a new entry in the detail table.
	 * 
	 * 
	 * @param editedArzi
	 */
	public void updateAnExistingArzi(Arzi editedArzi);
	

}
