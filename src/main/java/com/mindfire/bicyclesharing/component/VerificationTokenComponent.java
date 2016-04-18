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

package com.mindfire.bicyclesharing.component;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.VerificationToken;
import com.mindfire.bicyclesharing.repository.VerificationTokenRepository;

/**
 * VerificationTokenComponent class is used for interacting with corresponding
 * repository class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class VerificationTokenComponent {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	/**
	 * This method is used to find the verification object by token.
	 * 
	 * @param token
	 *            verification token
	 * @return {@link VerificationToken} object
	 */
	public VerificationToken mapVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	/**
	 * This method is used to save verification token.
	 * 
	 * @param verificationToken
	 *            VerificationToken object.
	 * @return {@link VerificationToken} object
	 */
	public VerificationToken saveVerificationToken(VerificationToken verificationToken) {
		try {
			logger.info("Saved new verification token for user.");
			return verificationTokenRepository.save(verificationToken);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}
}
