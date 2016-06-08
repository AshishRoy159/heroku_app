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

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;

/**
 * ResendVerificationTokenEvent class extends ApplicationEvent which is invoked
 * when a resend request for verification event occurs
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@SuppressWarnings("serial")
public class ResendVerificationTokenEvent extends ApplicationEvent {

	private final Locale locale;
	private final VerificationToken newToken;
	private final User user;

	/**
	 * ResendVerificationTokenEvent constructor
	 * 
	 * @param locale
	 *            to tailor information for the user
	 * @param newToken
	 *            the new token generated
	 * @param user
	 *            User object
	 */
	public ResendVerificationTokenEvent(final Locale locale, final VerificationToken newToken, final User user) {
		super(user);
		this.locale = locale;
		this.newToken = newToken;
		this.user = user;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @return the newToken
	 */
	public VerificationToken getNewToken() {
		return newToken;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
}
