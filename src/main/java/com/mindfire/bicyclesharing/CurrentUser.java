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

package com.mindfire.bicyclesharing;

import org.springframework.security.core.authority.AuthorityUtils;

import com.mindfire.bicyclesharing.model.User;

/**
 * This class extends {@link org.springframework.security.core.userdetails.User}
 * class and it contains the authentication details of the current logged in
 * user.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	private User user;

	public CurrentUser(User user) {
		super(user.getEmail(), user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getRole().getUserRole().toString()));
		this.user = user;
	}

	/**
	 * This method returns the object of the current user details.
	 * 
	 * @return user
	 */
	public User getUser() {
		return user;
	}
}
