/**
 *
 */
package com.example.medicalarzi.service;

import java.util.List;

import com.example.medicalarzi.model.Lookup;


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
	 * This method is responsible for getting the correct Lookup based on the lookup id.
	 *
	 * @param lookupId
	 *
	 * @return Lookup
	 */
	public Lookup getByLookupId(Long lookupId);

	/**
	 * This method is responsible for getting the list of lookups based on the lookup type.
	 *
	 * @param lookupType
	 *
	 * @return java.util.List
	 */
	public List<Lookup> getByLookupType(String lookupType);

	/**
	 * This method is responsible for getting the sorted list of lookups based on the
	 * lookup type based on the defined sorting sequence.
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

}
