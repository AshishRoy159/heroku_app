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
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

/**
 * PickUpPointDTO class is used for taking data from add new pickup point and
 * update pickup point details view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class PickUpPointDTO {

	@NotNull
	@Size(max = 250)
	private String location;

	@NotNull
	@NumberFormat
	@Min(1)
	@Max(999)
	private int maxCapacity;

	private Boolean isActive;

	@NumberFormat
	private int pickUpPointId;

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
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the maxCapacity
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}

	/**
	 * @param maxCapacity
	 *            the maxCapacity to set
	 */
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
}
