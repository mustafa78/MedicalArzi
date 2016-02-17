/**
 * 
 */
package com.example.medicalarzi.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.example.medicalarzi.dao.ArziMapper;
import com.example.medicalarzi.dao.LookupMapper;
import com.example.medicalarzi.model.Arzi;
import com.example.medicalarzi.model.ArziSearchCriteria;
import com.example.medicalarzi.model.ArziSearchResult;
import com.example.medicalarzi.service.ReviewerService;

/**
 * @author mkanchwa
 *
 */
public class ReviewerServiceImpl implements ReviewerService {
	
	public static Logger logger = (Logger) LogManager
			.getLogger(ReviewerServiceImpl.class);
	
	private ArziMapper arziMapper;
	
	private LookupMapper lookupMapper;

	/**
	 * 
	 */
	public ReviewerServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public ArziMapper getArziMapper() {
		return arziMapper;
	}

	public void setArziMapper(ArziMapper arziMapper) {
		this.arziMapper = arziMapper;
	}

	public LookupMapper getLookupMapper() {
		return lookupMapper;
	}

	public void setLookupMapper(LookupMapper lookupMapper) {
		this.lookupMapper = lookupMapper;
	}

	@Override
	public List<ArziSearchResult> searchArzisByCriteria(
			ArziSearchCriteria criteria) {
		
		List<ArziSearchResult> searchResults = arziMapper
				.selectArzisBySearchCriteria(criteria);
		
		return searchResults;
	}

	@Override
	public List<ArziSearchResult> retrieveArzisAssignedToReviewer(
			Long reviewerItsNumber) {
		
		List<ArziSearchResult> pendingTaskResults = arziMapper
				.selectArzisByReviewerItsNumber(reviewerItsNumber);
		
		return pendingTaskResults;
	}

	@Override
	public void updateAnExistingArzi(Arzi editedArzi) {
		logger.debug("Updating arzi with arzi id: \"" + editedArzi.getArziId()
				+ "\" " + "by the reviewer with ITS number: -> "
				+ editedArzi.getReviewerItsNumber() + " to status: ->"
				+ editedArzi.getCurrentStatus().getStatusDesc());

		arziMapper.updateArziHdrCurrentStatus(editedArzi);
		
		logger.debug("Arzi with \"" + editedArzi.getArziId()
				+ "\" updated successfully.");	
		
		logger.debug("Inserting a new record for arzi id: \""
				+ editedArzi.getArziId() + "\" into the detail table.");
		
		arziMapper.insertArziDetail(editedArzi);
		
		logger.debug("Inserted record successfully.");	
	}
	
}
