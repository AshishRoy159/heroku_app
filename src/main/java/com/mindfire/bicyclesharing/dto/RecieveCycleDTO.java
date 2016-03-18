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

import java.sql.Timestamp;

/**
 * RecieveDTO class is used for taking data from Receive Bicycle view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class RecieveCycleDTO {

	private String bookingId;
	private Timestamp actualInTime;
	private String returnedAt;
	private Boolean isOpen;
	private Boolean isActive;
	private Double amount;

	/**
	 * returns the booking Id
	 * @return bookingId
	 */
	public String getBookingId() {
		return bookingId;
	}

	/**
	 * sets the booking Id
	 * @param bookingId
	 */
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	/**
	 * returns the actual in time of the bicycle
	 * @return actualInTime
	 */
	public Timestamp getActualInTime() {
		return actualInTime;
	}

	/**
	 * sets the actual in time of the bicycle.
	 * @param actualInTime
	 */
	public void setActualInTime(Timestamp actualInTime) {
		this.actualInTime = actualInTime;
	}

	/**
	 * returns the return time of the bicycle
	 * @return returnedAt
	 */
	public String getReturnedAt() {
		return returnedAt;
	}

	/**
	 * sets the return time of the bicycle.
	 * @param returnedAt
	 */
	public void setReturnedAt(String returnedAt) {
		this.returnedAt = returnedAt;
	}

	/**
	 * returns the boolean value of whether booking is open or not.
	 * @return
	 */
	public Boolean getIsOpen() {
		return isOpen;
	}

	/**
	 * sets the boolean value whether the boking is open or not.
	 * @param isOpen
	 */
	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * returns boolean value whether the cycle is active or not.
	 * @return
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * sets the condition to true or false based on the condition of the
	 * bicycle.
	 * @param isActive
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * returns the amount
	 * @return amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * sets the amount if applicable.
	 * @param amount
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
