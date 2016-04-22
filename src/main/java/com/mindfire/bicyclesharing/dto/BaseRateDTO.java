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

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

/**
 * BaseRateDTO class is used for taking data from addBaseRate view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class BaseRateDTO {

	@NotNull
	@Length(min = 2, max = 15)
	private String groupType;

	@NotNull
	@NumberFormat
	@Min(1)
	private Double baseRate;

	/**
	 * 
	 * @return groupType
	 */
	public String getGroupType() {
		return groupType;
	}

	/**
	 * 
	 * @param groupType
	 *            the type of rate group
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * 
	 * @return baseRate
	 */
	public Double getBaseRate() {
		return baseRate;
	}

	/**
	 * 
	 * @param baseRate
	 *            the base rate to set
	 */
	public void setBaseRate(Double baseRate) {
		this.baseRate = baseRate;
	}
}
