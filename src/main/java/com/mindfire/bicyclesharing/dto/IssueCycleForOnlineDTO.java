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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

/**
 * IssueCycleForOnlineDTO class is used for taking data from issue bicycle view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class IssueCycleForOnlineDTO {

	@NotNull @Min(1)
	@NumberFormat
	private Long bookingId;
	
	@NotNull @Min(1)
	@NumberFormat 
	private Long bicycleId;

	/**
	 * @return the bookingId
	 */
	public Long getBookingId() {
		return bookingId;
	}

	/**
	 * @param bookingId
	 *            the bookingId to set
	 */
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
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
}
