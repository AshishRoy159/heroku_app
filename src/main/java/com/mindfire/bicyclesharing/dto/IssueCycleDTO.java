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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

/**
 * IssueCycleDTO class is used for taking data from the Issue Cycle view
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class IssueCycleDTO {

	@NotNull @Min(1)
	@NumberFormat
	private Long userId;
	
	@NotNull @Min(1)
	@NumberFormat
	private Long bicycleId;
	
	@NotNull @Min(1) @Max(15)
	@NumberFormat
	private int expectedInTime;

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
	 * @return the bicycleId
	 */
	public Long getBicycleId() {
		return bicycleId;
	}

	/**
	 * @param bicycleId
	 *            the bicycleId to set
	 */
	public void setBicycleId(Long bicycleId) {
		this.bicycleId = bicycleId;
	}

	/**
	 * @return the expectedInTime
	 */
	public int getExpectedInTime() {
		return expectedInTime;
	}

	/**
	 * @param expectedInTime
	 *            the expectedInTime to set
	 */
	public void setExpectedInTime(int expectedInTime) {
		this.expectedInTime = expectedInTime;
	}
}
