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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.ProofDetailComponent;
import com.mindfire.bicyclesharing.component.UserComponent;
import com.mindfire.bicyclesharing.dto.ForgotPasswordDTO;
import com.mindfire.bicyclesharing.dto.ManageRateGroupDTO;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
import com.mindfire.bicyclesharing.dto.RegistrationPaymentDTO;
import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.ProofDetail;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.VerificationTokenRepository;

/**
 * UserService class contains methods for user related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class UserService {

	@Autowired
	private UserComponent userComponent;

	@Autowired
	private ProofDetailComponent proofDetailComponent;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	private static final String DOCUMENTS_DIR = "src/main/resources/documents";

	/**
	 * This method is used to save the user related data to the database
	 * 
	 * @param userDTO
	 *            the incoming user details
	 * @param regPaymentDTO
	 *            the incoming payment details
	 * @return WalletTransaction object
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	public WalletTransaction saveUserDetails(UserDTO userDTO, RegistrationPaymentDTO regPaymentDTO)
			throws ParseException {
		return userComponent.mapUserComponent(userDTO, regPaymentDTO);
	}

	/**
	 * This method is for updating the password of the user
	 * 
	 * @param password
	 *            the encrypted password
	 * @param userEmail
	 *            the email id of user
	 * @return Integer 0 or 1
	 */
	public int savePassword(String password, String userEmail) {
		return userComponent.mapPassword(password, userEmail);
	}

	/**
	 * This method is for updating the user data
	 * 
	 * @param userDTO
	 *            it contains user data
	 * @return Integer 0 or 1
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	public int updateUserDetail(UserDTO userDTO) throws ParseException {
		return userComponent.mapUpdateUserDetail(userDTO);
	}

	/**
	 * This method is used for storing a verification token for the user
	 * 
	 * @param user
	 *            the User details
	 * @param token
	 *            the token generated for the User
	 */
	public void createVerificationTokenForUser(final User user, final String token) {
		final VerificationToken myToken = new VerificationToken(token, user);

		try {
			tokenRepository.save(myToken);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used for getting user details from the verification token
	 * 
	 * @param verificationToken
	 *            the received verification token
	 * @return User object
	 */
	public User getUser(String verificationToken) {
		return tokenRepository.findByToken(verificationToken).getUser();
	}

	/**
	 * This method is used for getting Verification token details
	 * 
	 * @param verificationToken
	 *            the respective VerificationToken
	 * @return VerificationToken object
	 */
	public VerificationToken getVerificationToken(String verificationToken) {
		return tokenRepository.findByToken(verificationToken);
	}

	/**
	 * This method is used for saving the user details
	 * 
	 * @param user
	 *            User object
	 * @return {@link User}
	 */
	public User saveRegisteredUser(User user) {
		return userComponent.mapUser(user);
	}

	/**
	 * This method is used to get user details using user email
	 * 
	 * @param forgotPasswordDTO
	 *            the email id of User
	 * @return User object
	 */
	public User userDetailsByEmail(ForgotPasswordDTO forgotPasswordDTO) {
		return userComponent.retrieveUserDetails(forgotPasswordDTO);
	}

	/**
	 * This method is used to get user details using user id
	 * 
	 * @param userId
	 *            userId of User
	 * @return {@link User}
	 */
	public User userDetails(Long userId) {
		return userComponent.getUser(userId);
	}

	/**
	 * This method is used to get user details using user email
	 * 
	 * @param email
	 *            the email id of User
	 * @return User object
	 */
	public Optional<User> getUserByEmail(String email) {
		return userComponent.findUserByEmail(email);
	}

	/**
	 * This method is used to find the details of User by its mobile number
	 * 
	 * @param mobileNo
	 *            the mobile number of user
	 * @return {@link User} object
	 */
	public Optional<User> getUserByMobileNo(Long mobileNo) {
		return userComponent.findUserByMobileNo(mobileNo);
	}

	/**
	 * This method is used for storing a Password Reset token for the user
	 * 
	 * @param user
	 *            User object
	 * @param token
	 *            the generated token
	 */
	public void createResetPasswordTokenForUser(final User user, final String token) {
		final VerificationToken myToken = new VerificationToken(token, user);

		try {
			tokenRepository.save(myToken);
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used for getting all users detail.
	 * 
	 * @return User list
	 */
	public List<User> getAllUsers() {
		return userComponent.mapAllUserDetails();
	}

	/**
	 * This method is used to update the role of an User
	 * 
	 * @param manageRoleDTO
	 *            user role related data
	 * @return Integer 0 or 1
	 */
	public int updateUserRole(ManageRoleDTO manageRoleDTO) {
		return userComponent.mapUpdateRole(manageRoleDTO);
	}

	/**
	 * This method is used to update user approval field.
	 * 
	 * @param id
	 *            user id
	 * @return {@link User} object
	 */
	public User setApproval(Long id) {
		return userComponent.mapApproval(id);
	}

	/**
	 * This method is used to update user enable field.
	 * 
	 * @param id
	 *            user id
	 * @return {@link User} object
	 */
	public User setEnable(Long id) {
		return userComponent.mapIsActive(id);
	}

	/**
	 * This method saves the user verification document on server storage
	 * 
	 * @param userDTO
	 *            details of the user
	 * @throws IOException
	 *             may occur while storing the document
	 */
	public void saveUserDocument(UserDTO userDTO) throws IOException {
		Path documentsDir = Files.createDirectories(Paths.get(DOCUMENTS_DIR + "/" + userDTO.getEmail()));
		Path document = documentsDir.resolve(userDTO.getDocument().getOriginalFilename());
		Files.deleteIfExists(document);
		File dest = document.toAbsolutePath().toFile();

		userDTO.getDocument().transferTo(dest);
	}

	/**
	 * This method is used to assign rate group to user.
	 * 
	 * @param manageRateGroupDTO
	 *            the incoming rate group details
	 * @return {@link User} object
	 */
	public User UpdateRateGroup(ManageRateGroupDTO manageRateGroupDTO) {
		return userComponent.assignRateGroup(manageRateGroupDTO);
	}

	/**
	 * This method is used to get all user's details
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link User} {@link DataTablesOutput}
	 */
	public DataTablesOutput<User> findAllUsers(DataTablesInput input) {
		return userComponent.getAllUsers(input);
	}

	/**
	 * This method is used to get the user proof details by proof no.
	 * 
	 * @param proofNo
	 *            User's proof number
	 * @return {@link ProofDetail} object
	 */
	public Optional<ProofDetail> getProofDetailByProofNo(String proofNo) {
		return proofDetailComponent.findProofDetailByProofNo(proofNo);
	}
}
