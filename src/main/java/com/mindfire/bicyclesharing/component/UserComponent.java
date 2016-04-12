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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.ForgotPasswordDTO;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
import com.mindfire.bicyclesharing.dto.RegistrationPaymentDTO;
import com.mindfire.bicyclesharing.dto.UserDTO;
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
	 * @throws IOException
	 */
	public WalletTransaction mapUserComponent(UserDTO userDTO, RegistrationPaymentDTO regPaymentDTO)
			throws ParseException, IOException {

		User newUser = new User();

		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setEmail(userDTO.getEmail());
		newUser.setUserAddress(userDTO.getUserAddress());
		newUser.setMobileNo(userDTO.getMobileNo());
		newUser.setDateOfBirth(simpleDateFormat.parse(userDTO.getDateOfBirth()));

		ProofDetail proofDetail = new ProofDetail();
		proofDetail.setProofType(userDTO.getProofType());
		proofDetail.setProofNo(userDTO.getProofNo());
		proofDetail.setDocument(userDTO.getDocument().getOriginalFilename());

		Wallet wallet = new Wallet();
		wallet.setBalance(regPaymentDTO.getAmount());

		WalletTransaction transaction = new WalletTransaction();
		transaction.setMode(regPaymentDTO.getMode());
		transaction.setType("REGISTRATION");
		transaction.setAmount(regPaymentDTO.getAmount());

		proofDetailRepository.save(proofDetail);

		newUser.setProofDetail(proofDetail);
		newUser.setRole(roleRepository.findByUserRole("USER"));
		newUser.setRateGroup(rateGroupRepository.findByGroupTypeAndIsActive("USER", true));
		userRepository.save(newUser);

		wallet.setUser(newUser);
		walletRepository.save(wallet);

		transaction.setWallet(wallet);
		transactionRepository.save(transaction);

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

		return userRepository.updatePassword(passEncoder.encode(password), userEmail);
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
		return userRepository.updateUser(userDTO.getFirstName(), userDTO.getLastName(),
				simpleDateFormat.parse(userDTO.getDateOfBirth()), userDTO.getMobileNo(), userDTO.getUserAddress(),
				userDTO.getEmail());
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
		return userRepository.save(user);
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

		if (pickUpPointManager != null) {
			pickUpPointManagerRepository.deleteByUser(pickUpPointManager.getUser());
		}

		Role userRole = roleRepository.findByRoleId(manageRoleDTO.getUserRoleId());

		return userRepository.updateUserRole(userRole, manageRoleDTO.getUserId());
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
	 * This method is used to approve the user.
	 * 
	 * @param id
	 *            user id
	 * @return {@link User} object
	 */
	public User mapApproval(Long id) {
		User user = userRepository.findByUserId(id);
		user.setIsApproved(true);

		return userRepository.save(user);
	}
}
