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
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.model.User;
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

	/**
	 * This method is used to find the rate group by groupType
	 * 
	 * @param user
	 *            User object
	 * @return {@link RateGroup} object
	 */
	public RateGroup mapRateGroup(User user) {
		return rateGroupRepository.findByGroupType(user.getRateGroup().getGroupType());
	}
}
