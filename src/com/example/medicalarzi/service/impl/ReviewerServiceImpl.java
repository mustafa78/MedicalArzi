/**
 * 
 */
package com.example.medicalarzi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.example.medicalarzi.dao.ArziMapper;
import com.example.medicalarzi.dao.LookupMapper;
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

	
}
