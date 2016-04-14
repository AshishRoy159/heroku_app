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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.RateGroupDTO;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.repository.BaseRateRepository;
import com.mindfire.bicyclesharing.repository.RateGroupRepository;

/**
 * RateGroupComponent class is used for interacting with corresponding
 * repository class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class RateGroupComponent {

	@Autowired
	private RateGroupRepository rateGroupRepository;
	
	@Autowired
	private BaseRateRepository baseRateRepository;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * This method is used to find the rate group by groupType
	 * 
	 * @param user
	 *            User object
	 * @return {@link RateGroup} object
	 */
	public RateGroup mapRateGroup(User user) {
		return rateGroupRepository.findByGroupTypeAndIsActive(user.getRateGroup().getGroupType(), true);
	}
	
	public RateGroup mapNewRateGroupDetails(RateGroupDTO rateGroupDTO) throws ParseException{
		RateGroup rateGroup = new RateGroup();
		rateGroup.setDiscount(rateGroupDTO.getDiscount());
		rateGroup.setEffectiveFrom(simpleDateFormat.parse(rateGroupDTO.getEffectiveFrom()));
		rateGroup.setGroupType(rateGroupDTO.getGroupType());
		rateGroup.setBaseRateBean(baseRateRepository.findByGroupType("USER"));
		return rateGroupRepository.save(rateGroup);
	}
	
	/**
	 * This method is used to find all Rate Group
	 * @return {@link RateGroup} List
	 */
	public List<RateGroup> getAllRateGroup () {
		return rateGroupRepository.findAll();
	}
}
