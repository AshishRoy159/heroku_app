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
	 * @return the bookingId
	 */
	public String getBookingId() {
		return bookingId;
	}

	/**
	 * @param bookingId
	 *            the bookingId to set
	 */
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	/**
	 * @return the actualInTime
	 */
	public Timestamp getActualInTime() {
		return actualInTime;
	}

	/**
	 * @param actualInTime
	 *            the actualInTime to set
	 */
	public void setActualInTime(Timestamp actualInTime) {
		this.actualInTime = actualInTime;
	}

	/**
	 * @return the returnedAt
	 */
	public String getReturnedAt() {
		return returnedAt;
	}

	/**
	 * @param returnedAt
	 *            the returnedAt to set
	 */
	public void setReturnedAt(String returnedAt) {
		this.returnedAt = returnedAt;
	}

	/**
	 * @return the isOpen
	 */
	public Boolean getIsOpen() {
		return isOpen;
	}

	/**
	 * @param isOpen
	 *            the isOpen to set
	 */
	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
}
