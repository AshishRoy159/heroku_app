/*
 * Copyright 2016 Mindfire Solutions
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mindfire.bicyclesharing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mindfire.bicyclesharing.model.Role;

/**
 * Repository for {@link Role} Entity used for CRUD operation on Role.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * This method is used to find the role of the user by its email id
	 * 
	 * @param email
	 *            the email id of User
	 * @return Role of the user
	 */
	@Query("select a.userRole from Role a, User b where b.email = ?1 and a.roleId = b.role.roleId")
	public List<String> findRoleByEmail(String email);

	/**
	 * This method is used to find the Role by roleID
	 * 
	 * @param roleId
	 *            id of the Role
	 * @return Role object
	 */
	public Role findByRoleId(Long roleId);

	/**
	 * This method is used to find the Role object by userRole
	 * 
	 * @param role
	 *            the userRole
	 * @return Role object
	 */
	public Role findByUserRole(String role);
}
