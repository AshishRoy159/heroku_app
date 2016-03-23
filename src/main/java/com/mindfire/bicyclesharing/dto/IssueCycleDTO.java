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
 * IssyeCycleDTO class is used for taking data from the Issue Cycle view
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class IssueCycleDTO {

	private String bookingId;
	private String userId;
	private String bicycleId;
	private String transactionId;
	private Timestamp bookingTime;
	private Timestamp actualOutTime;
	private Timestamp expectedInTime;
	private String pickedUpFrom;

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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the bicycleId
	 */
	public String getBicycleId() {
		return bicycleId;
	}

	/**
	 * @param bicycleId
	 *            the bicycleId to set
	 */
	public void setBicycleId(String bicycleId) {
		this.bicycleId = bicycleId;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the bookingTime
	 */
	public Timestamp getBookingTime() {
		return bookingTime;
	}

	/**
	 * @param bookingTime
	 *            the bookingTime to set
	 */
	public void setBookingTime(Timestamp bookingTime) {
		this.bookingTime = bookingTime;
	}

	/**
	 * @return the actualOutTime
	 */
	public Timestamp getActualOutTime() {
		return actualOutTime;
	}

	/**
	 * @param actualOutTime
	 *            the actualOutTime to set
	 */
	public void setActualOutTime(Timestamp actualOutTime) {
		this.actualOutTime = actualOutTime;
	}

	/**
	 * @return the expectedInTime
	 */
	public Timestamp getExpectedInTime() {
		return expectedInTime;
	}

	/**
	 * @param expectedInTime
	 *            the expectedInTime to set
	 */
	public void setExpectedInTime(Timestamp expectedInTime) {
		this.expectedInTime = expectedInTime;
	}

	/**
	 * @return the pickedUpFrom
	 */
	public String getPickedUpFrom() {
		return pickedUpFrom;
	}

	/**
	 * @param pickedUpFrom
	 *            the pickedUpFrom to set
	 */
	public void setPickedUpFrom(String pickedUpFrom) {
		this.pickedUpFrom = pickedUpFrom;
	}
}
