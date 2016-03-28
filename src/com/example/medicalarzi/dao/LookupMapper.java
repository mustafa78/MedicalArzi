package com.example.medicalarzi.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Location;
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
	 * @return java.util.List<com.example.medicalarzi.model.Lookup>
	 */
	public List<Lookup> selectByLookupType(
			@Param("lookupType") String lookupType);

	/**
	 *
	 * @return java.util.List<com.example.medicalarzi.model.Lookup>
	 */
	public List<Lookup> selectDistinctLookupTypes();

	/**
	 *
	 * @param lookupType
	 *
	 * @return java.util.List<com.example.medicalarzi.model.Lookup>
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
	 * This method is responsible for selecting all the medical conditions from
	 * the dimension table.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Condition>
	 */
	public List<Condition> selectAllMedicalConditions();
	
	/**
	 * This method is responsible for selecting all the distinct conditions from
	 * the fact table
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Condition>
	 */
	public List<Condition> selectAllDistinctCondsFromArzis();
	
	/**
	 * This method is responsible for selecting all the medical procedures from
	 * the dimension table.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Procedure>
	 */
	public List<Procedure> selectAllMedicalProcedures();
	
	/**
	 * This method is responsible for selecting all the distinct procedures from
	 * the fact table
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Procedure>
	 */
	public List<Procedure> selectAllDistinctProcsFromArzis();
	
	/**
	 * This method is responsible for selecting all the body parts from
	 * the dimension table.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.BodyPart>
	 */
	public List<BodyPart> selectAllBodyParts();
	
	/**
	 * This method is responsible for selecting all the distinct body parts from
	 * the fact table
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.BodyPart>
	 */
	public List<BodyPart> selectAllDistinctBdyPartsFromArzis();
	
	/**
	 * This method is responsible for selecting all the arzi types from
	 * the dimension table.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.ArziType>
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
	 * This method is responsible for selecting all the available jamaats. 
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
	
	/**
	 * This method is responsible for selecting all the distinct countries from
	 * the D_LOCATION table.
	 * 
	 * @return java.util.List<java.util.String>
	 */
	public List<String> selectAllDistinctCountries();

	/**
	 * This method is responsible for selecting all the distinct states based on
	 * the country from the D_LOCATION table.
	 * 
	 * @param country
	 * 
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> selectAllDistinctStatesByCountry(
			@Param("country") String country);

	/**
	 * This method is responsible for selecting all the cities based on the
	 * state and country from the D_LOCATION table.
	 * 
	 * @param state
	 * @param country
	 * 
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> selectAllCitiesByStateAndCountry(
			@Param("state") String state, @Param("country") String country);
	
	/**
	 * This method is responsible for selecting the location based on the city,
	 * state and country.
	 * 
	 * @param country
	 * @param state
	 * @param city
	 * 
	 * @return com.example.medicalarzi.model.Location
	 */
	public Location selectLocationByCityStateAndCountry(
			@Param("city") String city, @Param("state") String state,
			@Param("country") String country);
	
	/**
	 * This method is responsible for selecting the location based on the id.
	 * 
	 * @param locationId
	 * 
	 * @return com.example.medicalarzi.model.Location
	 */
	public Location selectLocationById(@Param("locationId") Long locationId); 
}