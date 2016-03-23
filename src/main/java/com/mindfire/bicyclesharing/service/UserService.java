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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.model.PasswordResetToken;
import com.mindfire.bicyclesharing.model.ProofDetail;
import com.mindfire.bicyclesharing.model.Role;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.VerificationToken;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.PasswordResetTokenRepository;
import com.mindfire.bicyclesharing.repository.ProofDetailRepository;
import com.mindfire.bicyclesharing.repository.RateGroupRepository;
import com.mindfire.bicyclesharing.repository.RoleRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.repository.VerificationTokenRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;
import com.mindfire.bicyclesharing.repository.WalletTransactionRepository;

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
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private ProofDetailRepository proofDetailRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RateGroupRepository rateGroupRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private WalletTransactionRepository transactionRepository;

	/**
	 * This method is used to save the user related data to the database
	 * 
	 * @param user
	 *            the user details
	 * @param proofDetail
	 *            user identification proof details
	 * @param wallet
	 *            Wallet details of the user
	 * @param transaction
	 *            WalletTransaction details of the user
	 * @return WalletTransaction object
	 */
	public WalletTransaction saveUserDetails(User user, ProofDetail proofDetail, Wallet wallet,
			WalletTransaction transaction) {
		proofDetailRepository.save(proofDetail);
		user.setProofDetail(proofDetail);
		user.setRole(roleRepository.findByUserRole("USER"));
		user.setRateGroup(rateGroupRepository.findByGroupType("USER"));
		userRepository.save(user);

		proofDetailRepository.save(proofDetail);
		user.setProofDetail(proofDetail);
		userRepository.save(user);

		wallet.setUser(user);
		walletRepository.save(wallet);

		transaction.setWallet(wallet);
		transactionRepository.save(transaction);

		return transaction;
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
		return userRepository.updatePassword(password, userEmail);
	}

	/**
	 * This method is for updating the user data
	 * 
	 * @param firstName
	 *            of the user
	 * @param lastName
	 *            of the user
	 * @param dateOfBirth
	 *            of the user
	 * @param mobileNo
	 *            of the user
	 * @param userAddress
	 *            of the user
	 * @param email
	 *            of the user
	 * @return Integer 0 or 1
	 */
	public int updateUserDetail(String firstName, String lastName, Date dateOfBirth, Long mobileNo, String userAddress,
			String email) {
		return userRepository.updateUser(firstName, lastName, dateOfBirth, mobileNo, userAddress, email);
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
		tokenRepository.save(myToken);
	}

	/**
	 * This method is used for generating new verification token for the user
	 * 
	 * @param existingVerificationToken
	 *            the existing expired token
	 * @return VerificationToken object
	 */
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		vToken = tokenRepository.save(vToken);
		return vToken;
	}

	/**
	 * This method is used for getting user details from the verification token
	 * 
	 * @param verificationToken
	 *            the received verification token
	 * @return User object
	 */
	public User getUser(String verificationToken) {
		User user = tokenRepository.findByToken(verificationToken).getUser();
		return user;
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
	 */
	public void saveRegisteredUser(User user) {
		userRepository.save(user);
	}

	/**
	 * This method is used to get user details using user email
	 * 
	 * @param userEmail
	 *            the email id of User
	 * @return User object
	 */
	public User userDetails(String userEmail) {
		return userRepository.findByEmail(userEmail);
	}

	/**
	 * This method is used to get user details using user id
	 * 
	 * @param id
	 *            userId of User
	 * @return User object
	 */
	public User userDetails(Long id) {
		return userRepository.findByUserId(id);
	}

	/**
	 * This method is used to get user details using user email
	 * 
	 * @param email
	 *            the email id of User
	 * @return User object
	 */
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findOneByEmail(email);
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
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}

	/**
	 * This method is used for getting all users detail.
	 * 
	 * @return User list
	 */
	public List<User> getAllUsers() {
		return userRepository.findAllByOrderByUserId();
	}

	/**
	 * This method is used to update the role of an User
	 * 
	 * @param userId
	 *            of the respective User
	 * @param userRoleId
	 *            id of the Role to be set
	 * @return Integer 0 or 1
	 */
	public int updateUserRole(Long userId, Role userRoleId) {
		return userRepository.updateUserRole(userRoleId, userId);
	}
}
