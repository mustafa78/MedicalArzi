/**
 *
 */
package com.example.medicalarzi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.medicalarzi.model.SecurityRole;

/**
 * @author mkanchwa
 *
 */
public interface SecurityMapper {

	/**
	 * This method is responsible for returning all the possible security roles
	 * for the patient.
	 * 
	 * @return
	 */
	public List<SecurityRole> selectAllRoles();

	/**
	 * This method is responsible for getting all the security roles for the
	 * patient.
	 * 
	 * @param ptntItsNumber
	 * @return
	 */
	public List<SecurityRole> selectSecurityRolesForUser(
			@Param("ptntItsNumber") Long ptntItsNumber);

}
