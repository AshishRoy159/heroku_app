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

package com.mindfire.bicyclesharing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.security.CurrentUser;

/**
 * This class implements {@link UserDetailsService} which is predefined in
 * spring security to perform some user security related operations.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	/**
	 * Default constructor of the service class
	 */
	public CustomUserDetailsService() {

	}

	/**
	 * Parameterized constructor of the service class
	 * 
	 * @param userService
	 *            {@link UserService} object
	 */
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
		return new CurrentUser(user);
	}
}
