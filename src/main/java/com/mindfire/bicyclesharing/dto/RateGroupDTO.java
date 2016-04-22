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

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

/**
 * RateGroupDTO class is used for taking data from add new rate group view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class RateGroupDTO {

	@NotNull @NumberFormat
	@Min(0) @Max(100)
	private Double discount;
	
	@NotNull @Length(min=2,max=25)
	private String groupType;
	
	@NotNull @Length(min=2,max=10)
	private String effectiveFrom;

	/**
	 * @return the discount
	 */
	public Double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}

	/**
	 * @param groupType
	 *            the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return the effectiveFrom
	 */
	public String getEffectiveFrom() {
		return effectiveFrom;
	}

	/**
	 * @param effectiveFrom
	 *            the effectiveFrom to set
	 */
	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}
}
