package com.example.medicalarzi.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Procedure;

/**
 * The SQL mappers xml should have the table names in CAPS as it is not working
 * if the table names are not CASE SENSITIVE. Most probably it should be in the
 * same case as defined in the database.
 *
 * @author mkanchwa
 *
 */
public interface LookupMapper {
	/**
	 *
	 * @param lookupType
	 *
	 * @return java.util.List
	 */
	public List<Lookup> selectByLookupType(
			@Param("lookupType") String lookupType);

	/**
	 *
	 * @return java.util.List
	 */
	public List<Lookup> selectDistinctLookupTypes();

	/**
	 *
	 * @param lookupType
	 *
	 * @return java.util.List
	 */
	public List<Lookup> selectByLookupTypeValueSort(
			@Param("lookupType") String lookupType);

	/**
	 *
	 * @param paramMap
	 *
	 * @return com.example.medicalarzi.model.Lookup
	 */
	public Lookup selectByLookupTypeAndLookupValue(
			@Param("lookupType") String lookupType,
			@Param("lookupValue") String lookupValue);

	/**
	 *
	 * @param lookupId
	 *
	 * @return com.example.medicalarzi.model.Lookup
	 */
	public Lookup selectByLookupId(@Param("lookupId") Long lookupId);

	/**
	 *
	 * @param lookupValue
	 *
	 * @return com.example.medicalarzi.model.Lookup
	 */
	public Lookup selectByLookupValue(@Param("lookupValue") String lookupValue);

	/**
	 *
	 * @param record
	 *
	 * @return int
	 */
	public int insertLookup(Lookup record);

	/**
	 *
	 * @param record
	 *
	 * @return int
	 */
	public int updateLookup(Lookup record);
	
	/**
	 * 
	 * @return com.example.medicalarzi.model.Condition
	 */
	public List<Condition> selectAllMedicalConditions();
	
	/**
	 * 
	 * @return com.example.medicalarzi.model.Procedure
	 */
	public List<Procedure> selectAllMedicalProcedures();
	
	/**
	 * 
	 * @return com.example.medicalarzi.model.BodyPart
	 */
	public List<BodyPart> selectAllBodyParts();
	
	/**
	 * 
	 * @return com.example.medicalarzi.model.ArziType
	 */
	public List<ArziType> selectAllArziTypes();
	
	/**
	 * This method is responsible for selecting the custom Hijri date based on
	 * the gregorian calendar date.
	 * 
	 * @param calDate
	 * 
	 * @return com.example.medicalarzi.model.GregHijDate
	 */
	public GregHijDate selectGregHijBasedOnCalDt(Date calDate);
	
	/**
	 * This method is responsible for selecting the custom Hijri date based on
	 * the id
	 * 
	 * @param dateId
	 * 
	 * @return com.example.medicalarzi.model.GregHijDate
	 */
	public GregHijDate selectGregHijDateById(@Param("dateId") Long dateId);
	
	/**
	 * 
	 * @return com.example.medicalarzi.model.Jamaat
	 */
	public List<Jamaat> selectAllJamaats();
	
	/**
	 * This method is responsible for selecting a particular jamaat based on the
	 * id
	 * 
	 * @param jamaatId
	 * 
	 * @return com.example.medicalarzi.model.Jamaat
	 */
	public Jamaat selectJamaatById(@Param("jamaatId") Long jamaatId);
}