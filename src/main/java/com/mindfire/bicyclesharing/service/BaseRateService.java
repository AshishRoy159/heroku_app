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

package com.mindfire.bicyclesharing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.BaseRateComponent;
import com.mindfire.bicyclesharing.dto.BaseRateDTO;
import com.mindfire.bicyclesharing.model.BaseRate;

/**
 * BaseRateService class contains methods for Base rate related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class BaseRateService {

	@Autowired
	private BaseRateComponent baseRateComponent;

	/**
	 * This method is used to find the base rate based on the group type.
	 * 
	 * @param groupType
	 *            String value
	 * @return {@link BaseRate} object
	 */
	public BaseRate getBaseRate(String groupType) {
		return baseRateComponent.mapBaseRate(groupType);

	}

	/**
	 * This method is used to update the base rate.
	 * 
	 * @param baseRateDTO
	 *            this object contains base rate related data.
	 * @return {@link BaseRate} object
	 */
	public String updateBaseRate(BaseRateDTO baseRateDTO) {
		return baseRateComponent.mapUpdateBaseRate(baseRateDTO);
	}
}
