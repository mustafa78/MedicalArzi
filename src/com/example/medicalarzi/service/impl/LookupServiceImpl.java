/**
 *
 */
package com.example.medicalarzi.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.example.medicalarzi.dao.LookupMapper;
import com.example.medicalarzi.dao.SecurityMapper;
import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.model.SecurityRole;
import com.example.medicalarzi.service.LookupService;

/**
 * @author Mkanchwa
 *
 */
public class LookupServiceImpl implements LookupService {

	public static Logger logger = (Logger) LogManager
			.getLogger(LookupServiceImpl.class);

	private LookupMapper lookupMapper;

	private SecurityMapper securityMapper;
	
	/**
	 * @return the lookupMapper
	 */
	public LookupMapper getLookupMapper() {
		return lookupMapper;
	}

	/**
	 * @param lookupMapper the lookupMapper to set
	 */
	public void setLookupMapper(LookupMapper lookupMapper) {
		this.lookupMapper = lookupMapper;
	}

	/**
	 * @return the securityMapper
	 */
	public SecurityMapper getSecurityMapper() {
		return securityMapper;
	}

	/**
	 * @param securityMapper the securityMapper to set
	 */
	public void setSecurityMapper(SecurityMapper securityMapper) {
		this.securityMapper = securityMapper;
	}

	/**
	 *
	 */
	public LookupServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.example.medicalarzi.service.LookupService#getListOfLookupTypes()
	 */
	@Override
	public List<Lookup> getListOfLookupTypes() {
		return lookupMapper.selectDistinctLookupTypes();
	}

	/* (non-Javadoc)
	 * @see com.example.medicalarzi.service.LookupService#getByLookupId(java.lang.Long)
	 */
	@Override
	public Lookup getByLookupId(Long lookupId) {
		return lookupMapper.selectByLookupId(lookupId);
	}

	/* (non-Javadoc)
	 * @see com.example.medicalarzi.service.LookupService#getByLookupType(java.lang.String)
	 */
	@Override
	public List<Lookup> getByLookupType(String lookupType) {
		List<Lookup> listOfLookups = lookupMapper.selectByLookupType(lookupType);
		return listOfLookups;
	}

	/* (non-Javadoc)
	 * @see com.example.medicalarzi.service.LookupService#getByLookupTypeValueSort(java.lang.String)
	 */
	@Override
	public List<Lookup> getByLookupTypeValueSort(String lookupType) {
		return lookupMapper.selectByLookupTypeValueSort(lookupType);
	}

	/* (non-Javadoc)
	 * @see com.example.medicalarzi.service.LookupService#updateLookup(com.example.medicalarzi.model.Lookup)
	 */
	@Override
	public void updateLookup(Lookup lookup) {
		lookupMapper.updateLookup(lookup);
	}

	@Override
	public List<BodyPart> getListOfAllBodyParts() {
		List<BodyPart> listOfBodyParts = lookupMapper.selectAllBodyParts();
		return listOfBodyParts;
	}

	@Override
	public List<Condition> getListOfAllMedicalConditions() {
		List<Condition> listOfMedicalConditions = lookupMapper.selectAllMedicalConditions();
		return listOfMedicalConditions;
	}

	@Override
	public List<Procedure> getListOfAllMedicalProcedures() {
		List<Procedure> listOfProcedures = lookupMapper.selectAllMedicalProcedures();
		return listOfProcedures;
	}

	@Override
	public List<ArziType> getListOfAllArziTypes() {
		List<ArziType> listOfArziTypes = lookupMapper.selectAllArziTypes();
		return listOfArziTypes;
	}

	@Override
	public GregHijDate getRequestedGregorianHijriCalendar(Date calendarDate) {
		 return lookupMapper.selectGregHijBasedOnCalDt(calendarDate);
	}

	@Override
	public List<Jamaat> getListOfAllJamaats() {
		List<Jamaat> listOfJamaats =  lookupMapper.selectAllJamaats();
		return listOfJamaats;
	}

	@Override
	public SecurityRole getSecurityRoleById(Long securityRole) {
		return securityMapper.selectSecurityRoleById(securityRole);
	}

}
