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

import org.hibernate.validator.constraints.Length;

/**
 * ChangePasswordDTO class is used for taking data from changePassword view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class ChangePasswordDTO {

	@NotNull
	@Length(min = 4, max = 16)
	private String oldPassword;

	@NotNull
	@Pattern(regexp = "[^ \t\n\f\r]{4,16}")
	private String newPassword;

	@NotNull
	@Pattern(regexp = "[^ \t\n\f\r]{4,16}")
	private String cnfPassword;

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

	/**
	 * @param oldPassword
	 *            the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword
	 *            the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the cnfPassword
	 */
	public String getCnfPassword() {
		return cnfPassword;
	}

	/**
	 * @param cnfPassword
	 *            the cnfPassword to set
	 */
	public void setCnfPassword(String cnfPassword) {
		this.cnfPassword = cnfPassword;
	}
}
