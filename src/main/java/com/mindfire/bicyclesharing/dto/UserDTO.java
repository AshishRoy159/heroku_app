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

package com.mindfire.bicyclesharing.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 * UserDTO class is used for taking data from the registration view
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class UserDTO {

	@NotNull
	@Pattern(regexp = "[a-zA-Z]{2,20}")
	@Size(min = 2, max = 20)
	private String firstName;

	@NotNull
	@Pattern(regexp = "[a-zA-Z]{2,20}")
	private String lastName;

	@NotNull
	@NumberFormat
	private Long mobileNo;

	@NotNull
	private String dateOfBirth;

	@NotNull
	@Email
	@Length(max = 50)
	private String email;

	@NotNull
	@Length(max = 250)
	private String userAddress;

	private String proofType;

	@Pattern(regexp = "[a-zA-Z0-9]{2,20}")
	private String proofNo;

	private MultipartFile document;

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the mobileNo
	 */
	public Long getMobileNo() {
		return mobileNo;
	}

	/**
	 * @param mobileNo
	 *            the mobileNo to set
	 */
	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the userAddress
	 */
	public String getUserAddress() {
		return userAddress;
	}

	/**
	 * @param userAddress
	 *            the userAddress to set
	 */
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	/**
	 * @return the proofType
	 */
	public String getProofType() {
		return proofType;
	}

	/**
	 * @param proofType
	 *            the proofType to set
	 */
	public void setProofType(String proofType) {
		this.proofType = proofType;
	}

	/**
	 * @return the proofNo
	 */
	public String getProofNo() {
		return proofNo;
	}

	/**
	 * @param proofNo
	 *            the proofNo to set
	 */
	public void setProofNo(String proofNo) {
		this.proofNo = proofNo;
	}

	/**
	 * @return the document
	 */
	public MultipartFile getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(MultipartFile document) {
		this.document = document;
	}
}
