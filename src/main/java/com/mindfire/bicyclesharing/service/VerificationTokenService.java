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

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.VerificationTokenComponent;
import com.mindfire.bicyclesharing.model.VerificationToken;

/**
 * VerificationTokenService class contains methods for VerificationToken related
 * operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class VerificationTokenService {

	@Autowired
	private VerificationTokenComponent verificationTokenComponent;

	/**
	 * This method is used to get verification token details form token String
	 * 
	 * @param token
	 *            the token String
	 * @return {@link VerificationToken} object
	 */
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenComponent.mapVerificationToken(token);
	}

	/**
	 * This method is used for generating new verification token for the user
	 * 
	 * @param existingVerificationToken
	 *            the existing expired token
	 * @return VerificationToken object
	 */
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = verificationTokenComponent.mapVerificationToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		return verificationTokenComponent.saveVerificationToken(vToken);
	}
}
