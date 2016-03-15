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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.ForgotPasswordDTO;
import com.mindfire.bicyclesharing.dto.UserDTO;
import com.mindfire.bicyclesharing.model.ProofDetail;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.UserService;

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
	private UserService userService;

	/**
	 * This method is used for receiving the data from UserDTO object and set
	 * the data to the corresponding entity classes
	 * 
	 * @param userDTO
	 * @return user Returns an User object
	 */
	public User mapUserComponent(UserDTO userDTO) {

		User newUser = new User();

		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setEmail(userDTO.getEmail());
		newUser.setUserAddress(userDTO.getUserAddress());
		newUser.setMobileNo(userDTO.getMobileNo());
		newUser.setDateOfBirth(userDTO.getDateOfBirth());

		ProofDetail proofDetail = new ProofDetail();
		proofDetail.setProofType(userDTO.getProofType());
		proofDetail.setProofNo(userDTO.getProofNo());
		proofDetail.setDocument(userDTO.getDocument());

		RateGroup rateGroup = new RateGroup();
		rateGroup.setGroupType("user-type");

		return userService.saveUserDetails(newUser, proofDetail, rateGroup);
	}

	/**
	 * This method receives the data from the SetPasswordDTO class and sets the
	 * data to the corresponding entity class
	 * 
	 * @param setPasswordDTO
	 * @return int either 0 or 1
	 */
	public int mapPassword(String password, String userEmail) {
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();

		return userService.savePassword(passEncoder.encode(password), userEmail);
	}

	/**
	 * This method receives the data from the UserDTO class and sets the data to
	 * the corresponding entity class
	 * 
	 * @param userDTO
	 * @return Integer 0 or 1
	 */
	public int mapUpdateUserDetail(UserDTO userDTO) {
		return userService.updateUserDetail(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getDateOfBirth(),
				userDTO.getMobileNo(), userDTO.getUserAddress(), userDTO.getEmail());
	}

	/**
	 * This method receives the data from the ForgotPasswordDTO class and
	 * retrieves the user data corresponding to the email in the dto.
	 * 
	 * @param forgotPasswordDTO
	 * @return User Object
	 */
	public User retrieveUserPassword(ForgotPasswordDTO forgotPasswordDTO) {
		return userService.userDetails(forgotPasswordDTO.getEmail());
	}
}
