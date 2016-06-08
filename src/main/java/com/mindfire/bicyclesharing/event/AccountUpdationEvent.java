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

package com.mindfire.bicyclesharing.event;

import org.springframework.context.ApplicationEvent;

import com.mindfire.bicyclesharing.model.User;

/**
 * OnRegistrationCompleteEvent class extends ApplicationEvent which is invoked
 * when a registration event occurs
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@SuppressWarnings("serial")
public class AccountUpdationEvent extends ApplicationEvent {

	private final User user;
	private final String status;

	/**
	 * OnRegistrationCompleteEvent constructor
	 * 
	 * @param user
	 *            User object
	 * @param status
	 *            updated status
	 */
	public AccountUpdationEvent(final User user, final String status) {
		super(user);
		this.user = user;
		this.status = status;
	}

	/**
	 * @return the approval
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
}
