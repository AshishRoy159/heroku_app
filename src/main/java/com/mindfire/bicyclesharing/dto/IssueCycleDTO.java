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
	 * returns bookingId
	 * @return bokingId
	 */
	public String getBookingId() {
		return bookingId;
	}

	/**
	 * 
	 * @param bookingId
	 *            sets the booking Id
	 */
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	/**
	 * returnsUserId
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 *            sets the userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * returns the BicycleId
	 * 
	 * @return bicycleId
	 */
	public String getBicycleId() {
		return bicycleId;
	}

	/**
	 * Sets the bicycleId
	 * 
	 * @param bicycleId
	 */
	public void setBicycleId(String bicycleId) {
		this.bicycleId = bicycleId;
	}

	/**
	 * returns the TransactionId
	 * 
	 * @return TransactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * sets the transactionId
	 * 
	 * @param transactionId
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * returns the Booking Time
	 * 
	 * @return bookingTime
	 */
	public Timestamp getBookingTime() {
		return bookingTime;
	}

	/**
	 * sets the Booking Time
	 * 
	 * @param bookingTime
	 */
	public void setBookingTime(Timestamp bookingTime) {
		this.bookingTime = bookingTime;
	}

	/**
	 * returns the actual time
	 * 
	 * @return actualOutTime
	 */
	public Timestamp getActualOutTime() {
		return actualOutTime;
	}

	/**
	 * sets the actual out time.
	 * 
	 * @param actualOutTime
	 */
	public void setActualOutTime(Timestamp actualOutTime) {
		this.actualOutTime = actualOutTime;
	}

	/**
	 * returns the expected return time.
	 * 
	 * @return expectedInTime
	 */
	public Timestamp getExpectedInTime() {
		return expectedInTime;
	}

	/**
	 * sets the expected return time
	 * 
	 * @param expectedInTime
	 */
	public void setExpectedInTime(Timestamp expectedInTime) {
		this.expectedInTime = expectedInTime;
	}

	/**
	 * returns the pickup point id from where bicycle is issued.
	 * 
	 * @return pickedUpFrom
	 */
	public String getPickedUpFrom() {
		return pickedUpFrom;
	}

	/**
	 * sets the pick up point id from where the bicycle is being issued.
	 * 
	 * @param pickedUpFrom
	 */
	public void setPickedUpFrom(String pickedUpFrom) {
		this.pickedUpFrom = pickedUpFrom;
	}

}
