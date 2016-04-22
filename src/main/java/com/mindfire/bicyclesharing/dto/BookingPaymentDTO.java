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
 * BookingPaymentDTO class is used for taking data from Booking Payment view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class BookingPaymentDTO {

	@NotNull
	@Min(5) @NumberFormat 
	private Double amount;
	
	@NotNull
	private String mode;
	
	@NotNull
	private Long userId;
	
	@NotNull
	private Long bicycleId;
	
	@NotNull @NumberFormat
	@Min(1) @Max(15)
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

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
}
