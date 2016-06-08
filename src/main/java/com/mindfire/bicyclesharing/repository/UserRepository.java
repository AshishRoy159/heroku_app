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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.bicyclesharing.model.Role;
import com.mindfire.bicyclesharing.model.User;

/**
 * Repository for {@link User} Entity used for CRUD operation on User.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface UserRepository extends DataTablesRepository<User, Long> {

	/**
	 * This method is used to find the details of one User by its email id
	 * 
	 * @param email
	 *            the email id of User
	 * @return User object
	 */
	Optional<User> findOneByEmail(String email);

	/**
	 * This method is used to find the details of User by its mobile number
	 * 
	 * @param mobileNo
	 *            the mobile number of user
	 * @return {@link User} object
	 */
	Optional<User> findByMobileNo(Long mobileNo);

	/**
	 * This method is used to find the details of one User by its userId
	 * 
	 * @param id
	 *            the userID
	 * @return User object
	 */
	public User findByUserId(Long id);

	/**
	 * This method is used to find the details of one User by its email id
	 * 
	 * @param email
	 *            the email id of User
	 * @return User object
	 */
	public User findByEmail(String email);

	/**
	 * This method is used to find the details of all users
	 * 
	 * @return User List
	 */
	public List<User> findAllByOrderByUserId();

	/**
	 * This method is used to update the password of the user
	 * 
	 * @param password
	 *            of User
	 * @param userEmail
	 *            of User
	 * @return Integer 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update User u set u.password =:password where u.email =:userEmail")
	public int updatePassword(@Param("password") String password, @Param("userEmail") String userEmail);

	/**
	 * This method is used to update the user details
	 * 
	 * @param firstName
	 *            of User
	 * @param lastName
	 *            of User
	 * @param dateOfBirth
	 *            of User
	 * @param mobileNo
	 *            of User
	 * @param userAddress
	 *            of User
	 * @param userEmail
	 *            of User
	 * @return Integer 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update User u set u.firstName =:firstName, u.lastName =:lastName, u.dateOfBirth =:dateOfBirth, u.mobileNo =:mobileNo, u.userAddress =:userAddress where u.email =:userEmail")
	public int updateUser(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("dateOfBirth") Date dateOfBirth, @Param("mobileNo") Long mobileNo,
			@Param("userAddress") String userAddress, @Param("userEmail") String userEmail);

	/**
	 * This method is used to update the user role
	 * 
	 * @param userRoleId
	 *            of User
	 * @param userId
	 *            of User
	 * @return Integer 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update User u set u.role =:userRoleId where u.userId =:userId")
	public int updateUserRole(@Param("userRoleId") Role userRoleId, @Param("userId") Long userId);
}
