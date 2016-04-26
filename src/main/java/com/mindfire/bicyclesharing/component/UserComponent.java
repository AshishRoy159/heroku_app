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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.bicyclesharing.dto.ForgotPasswordDTO;
import com.mindfire.bicyclesharing.dto.ManageRateGroupDTO;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
import com.mindfire.bicyclesharing.dto.RegistrationPaymentDTO;
import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.model.ProofDetail;
import com.mindfire.bicyclesharing.model.Role;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.ProofDetailRepository;
import com.mindfire.bicyclesharing.repository.RateGroupRepository;
import com.mindfire.bicyclesharing.repository.RoleRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;
import com.mindfire.bicyclesharing.repository.WalletTransactionRepository;

/**
 * UserComponent class is used to get the data from the UserDTO class and set
 * the data to the corresponding Entity classes
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class UserComponent {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RateGroupRepository rateGroupRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private WalletTransactionRepository transactionRepository;

	@Autowired
	private ProofDetailRepository proofDetailRepository;

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * This method is used for receiving the data from UserDTO object and set
	 * the data to the corresponding entity classes
	 * 
	 * @param userDTO
	 *            the data from the view
	 * @param regPaymentDTO
	 *            the data from the view
	 * @return user Returns an User object
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	@Transactional
	public WalletTransaction mapUserComponent(UserDTO userDTO, RegistrationPaymentDTO regPaymentDTO)
			throws ParseException {
		User newUser = new User();
		ProofDetail proofDetail = new ProofDetail();
		Wallet wallet = new Wallet();
		WalletTransaction transaction = new WalletTransaction();

		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setEmail(userDTO.getEmail());
		newUser.setUserAddress(userDTO.getUserAddress());
		newUser.setMobileNo(userDTO.getMobileNo());
		newUser.setDateOfBirth(simpleDateFormat.parse(userDTO.getDateOfBirth()));

		proofDetail.setProofType(userDTO.getProofType());
		proofDetail.setProofNo(userDTO.getProofNo());
		proofDetail.setDocument(userDTO.getDocument().getOriginalFilename());

		wallet.setBalance(regPaymentDTO.getAmount());

		transaction.setMode(regPaymentDTO.getMode());
		transaction.setType("REGISTRATION");
		transaction.setAmount(regPaymentDTO.getAmount());

		try {
			proofDetailRepository.save(proofDetail);
			logger.info("Created new user proof detail record.");
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}

		newUser.setProofDetail(proofDetail);
		newUser.setRole(roleRepository.findByUserRole("USER"));
		newUser.setRateGroup(rateGroupRepository.findByGroupTypeAndIsActive("USER", true).getGroupType());

		try {
			userRepository.save(newUser);
			logger.info("Added new user details to the database.");
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		wallet.setUser(newUser);

		try {
			walletRepository.save(wallet);
			logger.info("Added new wallet for the new user.");
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		transaction.setWallet(wallet);

		try {
			transactionRepository.save(transaction);
			logger.info("Created new wallet transaction for user's registration.");
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		return transaction;
	}

	/**
	 * This method receives the data from the SetPasswordDTO class and sets the
	 * data to the corresponding entity class
	 * 
	 * @param password
	 *            the password user entered
	 * @param userEmail
	 *            email id of the user
	 * @return Integer either 0 or 1
	 */
	public int mapPassword(String password, String userEmail) {
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();

		try {
			logger.info("Updated user's password..");
			return userRepository.updatePassword(passEncoder.encode(password), userEmail);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method receives the data from the UserDTO class and sets the data to
	 * the corresponding entity class
	 * 
	 * @param userDTO
	 *            the data from the view
	 * @return Integer 0 or 1
	 * @throws ParseException
	 *             may occur while pasring from String to Date
	 */
	public int mapUpdateUserDetail(UserDTO userDTO) throws ParseException {

		try {
			logger.info("Updated user's details.");
			return userRepository.updateUser(userDTO.getFirstName(), userDTO.getLastName(),
					simpleDateFormat.parse(userDTO.getDateOfBirth()), userDTO.getMobileNo(), userDTO.getUserAddress(),
					userDTO.getEmail());
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * This method receives the data from the ForgotPasswordDTO class and
	 * retrieves the user data corresponding to the email in the dto.
	 * 
	 * @param forgotPasswordDTO
	 *            the data from the view
	 * @return User Object
	 */
	public User retrieveUserDetails(ForgotPasswordDTO forgotPasswordDTO) {
		return userRepository.findByEmail(forgotPasswordDTO.getEmail());
	}

	/**
	 * This method returns users detail based on userId
	 * 
	 * @param userId
	 *            id of the user
	 * @return {@link User} object
	 */
	public User getUser(Long userId) {
		return userRepository.findByUserId(userId);
	}

	/**
	 * This method is used to update user details
	 * 
	 * @param user
	 *            user object
	 * @return {@link User} object
	 */
	public User mapUser(User user) {
		user.setEnabled(true);

		try {
			logger.info("Updated user's status to enabled.");
			return userRepository.save(user);
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method receives the data from the ManageRoleDTO class and deletes
	 * any existing record from PickUpPointManager entity before changing the
	 * user role of the corresponding user id.
	 * 
	 * @param manageRoleDTO
	 *            the data from the view
	 * @return Integer 0 or 1
	 */
	public int mapUpdateRole(ManageRoleDTO manageRoleDTO) {
		PickUpPointManager pickUpPointManager = pickUpPointManagerRepository
				.findByUser(userRepository.findByUserId(manageRoleDTO.getUserId()));
		Role userRole = roleRepository.findByRoleId(manageRoleDTO.getUserRoleId());

		if (pickUpPointManager != null) {
			pickUpPointManagerRepository.deleteByUser(pickUpPointManager.getUser());
		}

		try {
			logger.info("Updated user's role.");
			return userRepository.updateUserRole(userRole, manageRoleDTO.getUserId());
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used for getting all users detail.
	 * 
	 * @return {@link User} List
	 */
	public List<User> mapAllUserDetails() {
		return userRepository.findAllByOrderByUserId();
	}

	/**
	 * This method is used to get user details using user email
	 * 
	 * @param email
	 *            the email id of User
	 * @return User object
	 */
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findOneByEmail(email);
	}

	/**
	 * This method is used to find the details of User by its mobile number
	 * 
	 * @param mobileNo
	 *            the mobile number of user
	 * @return {@link User} object
	 */
	public Optional<User> findUserByMobileNo(Long mobileNo) {
		return userRepository.findByMobileNo(mobileNo);
	}

	/**
	 * This method is used to approve the user.
	 * 
	 * @param id
	 *            user id
	 * @return {@link User} object
	 */
	public User mapApproval(Long id) {
		User user = userRepository.findByUserId(id);

		if (user.getIsApproved()) {
			logger.info("User is approved. Setting status to unapproved.");
			user.setIsApproved(false);
		} else {
			logger.info("User is not approved. Setting status to approved.");
			user.setIsApproved(true);
		}

		try {
			logger.info("Updated user's approval status.");
			return userRepository.save(user);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to enable or disable the user.
	 * 
	 * @param id
	 *            user id
	 * @return {@link User} object
	 */
	public User mapIsActive(Long id) {
		User user = userRepository.findByUserId(id);

		if (user.getEnabled()) {
			logger.info("User is enabled. Setting status to not enabled.");
			user.setEnabled(false);
		} else {
			logger.info("User is not enabled. Setting status to enabled.");
			user.setEnabled(true);
		}

		try {
			logger.info("Updated user's enabled status.");
			return userRepository.save(user);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to assign rate group to user.
	 * 
	 * @param manageRateGroupDTO
	 *            the incoming rate group details
	 * @return {@link User} object
	 */
	public User assignRateGroup(ManageRateGroupDTO manageRateGroupDTO) {
		User user = userRepository.findByUserId(manageRateGroupDTO.getUserId());
		user.setRateGroup(rateGroupRepository.findByRateGroupId(manageRateGroupDTO.getRateGroupId()).getGroupType());

		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to fetch all user's data
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link User} {@link DataTablesInput}
	 */
	public DataTablesOutput<User> getAllUsers(DataTablesInput input) {
		return userRepository.findAll(input);
	}
}
