/**
 *
 */
package com.example.medicalarzi.service;

import java.util.Date;
import java.util.List;

import com.example.medicalarzi.model.ArziType;
import com.example.medicalarzi.model.BodyPart;
import com.example.medicalarzi.model.Condition;
import com.example.medicalarzi.model.GregHijDate;
import com.example.medicalarzi.model.Jamaat;
import com.example.medicalarzi.model.Location;
import com.example.medicalarzi.model.Lookup;
import com.example.medicalarzi.model.Procedure;
import com.example.medicalarzi.model.SecurityRole;

/**
 * @author mkanchwa
 *
 */
public interface LookupService {

	/**
	 * This method is responsible for getting the list of lookup types.
	 *
	 * @return java.util.List
	 */
	public List<Lookup> getListOfLookupTypes();

	/**
	 * This method is responsible for getting the correct Lookup based on the
	 * lookup id.
	 *
	 * @param lookupId
	 *
	 * @return Lookup
	 */
	public Lookup getByLookupId(Long lookupId);

	/**
	 * This method is responsible for getting the list of lookups based on the
	 * lookup type.
	 *
	 * @param lookupType
	 *
	 * @return java.util.List
	 */
	public List<Lookup> getByLookupType(String lookupType);

	/**
	 * This method is responsible for getting the sorted list of lookups based
	 * on the lookup type based on the defined sorting sequence.
	 *
	 * @param lookupType
	 *
	 * @return java.util.List
	 */
	public List<Lookup> getByLookupTypeValueSort(String lookupType);

	/**
	 * This method is responsible for updating the lookup.
	 *
	 * @param lookup
	 */
	public void updateLookup(Lookup lookup);

	/**
	 * This method is responsible for getting all the list of the human body
	 * parts.
	 * 
	 * @return java.util.List
	 */
	public List<BodyPart> getListOfAllBodyParts();
	
	/**
	 * This method is responsible for getting all the distinct body
	 * parts from all the patient submitted arzis.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.BodyPart>
	 */
	public List<BodyPart> getListOfAllDistinctBodyPartsFromAllSubmittedArzis();	

	/**
	 * This method is responsible for getting all the medical conditions.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Condition>
	 */
	public List<Condition> getListOfAllMedicalConditions();
	
	/**
	 * This method is responsible for getting all the distinct medical
	 * conditions from all the patient submitted arzis.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Condition>
	 */
	public List<Condition> getListOfAllDistinctConditionsFromAllSubmittedArzis();

	/**
	 * This method is responsible for getting all the available medical
	 * procedures.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Procedure>
	 */
	public List<Procedure> getListOfAllMedicalProcedures();
	
	/**
	 * This method is responsible for getting all the distinct medical
	 * procedures from all the patient submitted arzis.
	 * 
	 * @return java.util.List<com.example.medicalarzi.model.Procedure>
	 */
	public List<Procedure> getListOfAllDistinctProceduresFromAllSubmittedArzis();	

	/**
	 * This method is responsible for getting all the possible arzi types.
	 * 
	 * @return java.util.List
	 */
	public List<ArziType> getListOfAllArziTypes();

	/**
	 * This method returns the custom GregHijDate object based on the given
	 * calendar date.
	 * 
	 * @param calendarDate
	 * 
	 * @return GregHijDate
	 */
	public GregHijDate getRequestedGregorianHijriCalendar(Date calendarDate);
	
	/**
	 * This method is responsible for getting all the possible jamaats.
	 * 
	 * @return java.util.List
	 */
	public List<Jamaat> getListOfAllJamaats();
	
	/**
	 * This method is responsible for getting a security role based on the
	 * security role id.
	 * 
	 * @param securityRole
	 * @return
	 */
	public SecurityRole getSecurityRoleById(Long securityRole);
	
	/**
	 * This method is responsible for getting a list of all the available
	 * countries.
	 * 
	 * @return
	 */
	public List<String> getListOfAllCountries();

	/**
	 * This method is responsible for getting a list of all the available states
	 * for a country based on the name of the country.
	 * 
	 * @param country
	 * 
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> getListOfAllStatesForCountry(String country);

	/**
	 * This method is responsible for getting a list of all the cities for a
	 * state/province in a country based on teh country and the state name.
	 * 
	 * @param state
	 * @param country
	 * 
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> getListOfAllCitiesForStateAndCountry(String state,
			String country);

	/**
	 * This method is responsible for getting the unique location for the user
	 * based on the city, state and country of his residence.
	 * 
	 * @param city
	 * @param state
	 * @param country
	 * 
	 * @return com.example.medicalarzi.model.Location
	 */
	public Location getLocationForAddress(String city, String state, String country);
	
	/**
	 * This method is responsible for getting the location based on the location
	 * id.
	 * 
	 * @param locationId
	 * 
	 * @return com.example.medicalarzi.model.Location
	 */
	public Location getLocation(Long locationId);
}
