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

import com.mindfire.bicyclesharing.model.Role;

/**
 * Repository for {@link Role} Entity
 * used for CRUD operation on Role.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	@Query("select a.userRole from Role a, User b where b.email = ?1 and a.roleId = b.role.roleId")
	public List<String> findRoleByEmail(String email);
	
	public Role findByUserRole(String role);
}
