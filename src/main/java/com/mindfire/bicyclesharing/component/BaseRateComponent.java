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

package com.mindfire.bicyclesharing.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.BaseRateDTO;
import com.mindfire.bicyclesharing.model.BaseRate;
import com.mindfire.bicyclesharing.repository.BaseRateRepository;

/**
 * BaseRateComponent class is used to get the data from the BaseRateDTO class
 * and set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class BaseRateComponent {

	@Autowired
	private BaseRateRepository baseRateRepository;

	/**
	 * This method is used to map the base rate details based in the group type.
	 * 
	 * @param groupType
	 *            String value
	 * @return {@link BaseRate} object
	 */
	public BaseRate mapBaseRate(String groupType) {
		return baseRateRepository.findByGroupType(groupType);
	}

	/**
	 * This method is used to update the base rate details.
	 * 
	 * @param baseRateDTO
	 *            this object holds the base rate related data.
	 * @return {@link BaseRate} object
	 */
	public String mapUpdateBaseRate(BaseRateDTO baseRateDTO) {
		BaseRate baseRate = baseRateRepository.findByGroupType(baseRateDTO.getGroupType());
		baseRate.setBaseRate(baseRateDTO.getBaseRate());

		try {
			baseRateRepository.save(baseRate);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return "Duplicate Data";
		}
		return "success";
	}
}
