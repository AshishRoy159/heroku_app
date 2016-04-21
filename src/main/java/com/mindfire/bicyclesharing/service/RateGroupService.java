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

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.RateGroupComponent;
import com.mindfire.bicyclesharing.dto.RateGroupDTO;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.model.User;

/**
 * RateGroupService class contains methods for rateGroup related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class RateGroupService {

	@Autowired
	private RateGroupComponent rateGroupComponent;

	/**
	 * This method is used to find the base rate.
	 * 
	 * @param user
	 *            User object
	 * @return {@link RateGroup} object
	 */
	public RateGroup getBaseRate(User user) {
		return rateGroupComponent.mapRateGroup(user);
	}

	/**
	 * This method is used to add add new rate group.
	 * 
	 * @param rateGroupDTO
	 *            the incoming data to be saved
	 * @return {@link RateGroup} object
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	public RateGroup addNewRateGroup(RateGroupDTO rateGroupDTO) throws ParseException {
		return rateGroupComponent.mapNewRateGroupDetails(rateGroupDTO);
	}

	/**
	 * This method is used to get all rate group details.
	 * 
	 * @return {@link RateGroup} List
	 */
	public List<RateGroup> getAllRateGroup() {
		return rateGroupComponent.getAllRateGroup();
	}

	/**
	 * This method is used to find all rate group based on isActive.
	 * 
	 * @param isActive
	 *            Boolean value
	 * @return {@link RateGroup} List
	 */
	public List<RateGroup> getAllRateGroupAndIsActive(Boolean isActive) {
		return rateGroupComponent.mapAllRateGroupAndIsActive(isActive);
	}

	/**
	 * This method is used to find RateGroup by id.
	 * 
	 * @param id
	 *            rateGroupId
	 * @return {@link RateGroup} object
	 */
	public RateGroup getRateGroupById(Integer id) {
		return rateGroupComponent.mapRateGroupById(id);
	}

	/**
	 * This method is used to update rate group type and create new rate group
	 * type.
	 * 
	 * @param rateGroupDTO
	 *            this object contains rate group related data.
	 * @return {@link RateGroup} object
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	public String updateRateGroup(RateGroupDTO rateGroupDTO) throws ParseException {
		return rateGroupComponent.mapUpdateRateGroupAndIsActive(rateGroupDTO);
	}
}
