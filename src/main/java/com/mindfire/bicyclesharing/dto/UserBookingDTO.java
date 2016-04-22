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
 * UserBookingDTO class is used for taking data from user booking view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class UserBookingDTO {

	@NotNull 
	private String bookingTime;
	
	@NotNull
	private String returnTime;
	
	@NotNull
	@NumberFormat
	private int pickUpPoint;

	/**
	 * @return the bookingTime
	 */
	public String getBookingTime() {
		return bookingTime;
	}

	/**
	 * @param bookingTime
	 *            the bookingTime to set
	 */
	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	/**
	 * @return the returnTime
	 */
	public String getReturnTime() {
		return returnTime;
	}

	/**
	 * @param returnTime
	 *            the returnTime to set
	 */
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	/**
	 * @return the pickUpPoint
	 */
	public int getPickUpPoint() {
		return pickUpPoint;
	}

	/**
	 * @param pickUpPoint
	 *            the pickUpPoint to set
	 */
	public void setPickUpPoint(int pickUpPoint) {
		this.pickUpPoint = pickUpPoint;
	}
}
