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

import org.springframework.format.annotation.NumberFormat;

/**
 * ManageRoleDTO class is used for taking data from manageRole view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class ManageRoleDTO {

	@NotNull
	@NumberFormat
	private Long userId;

	@NotNull
	@NumberFormat
	private Long userRoleId;

	@NumberFormat
	private int pickUpPointId;

	/**
	 * @return the pickUpPointId
	 */
	public int getPickUpPointId() {
		return pickUpPointId;
	}

	/**
	 * @param pickUpPointId
	 *            the pickUpPointId to set
	 */
	public void setPickUpPointId(int pickUpPointId) {
		this.pickUpPointId = pickUpPointId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userRoleId
	 */
	public Long getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param userRoleId
	 *            the userRoleId to set
	 */
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
}
