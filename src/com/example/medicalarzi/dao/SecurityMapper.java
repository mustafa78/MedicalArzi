/**
 *
 */
package com.example.medicalarzi.dao;

import java.util.List;

import com.example.medicalarzi.model.SecurityRole;

/**
 * @author mkanchwa
 *
 */
public interface SecurityMapper {

	public List<SecurityRole> selectAllRoles();

	public List<SecurityRole> selectSecurityRolesForUser(Long ptntItsNumber);

}
