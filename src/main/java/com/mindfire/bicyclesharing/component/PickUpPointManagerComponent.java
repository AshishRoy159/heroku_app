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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;

/**
 * PickUpPointManagerComponent class is used for interacting with corresponding
 * repository class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class PickUpPointManagerComponent {

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	/**
	 * This method is used to get all pickup point manager details
	 * 
	 * @return {@link PickUpPointManager} List
	 */
	public List<PickUpPointManager> getAllManager() {
		return pickUpPointManagerRepository.findAll();
	}

	/**
	 * This method is used to fetch pickup point manager details using user data
	 * 
	 * @param user
	 *            manager of the pickup point
	 * @return {@link PickUpPointManager} object
	 */
	public PickUpPointManager getPickUpPointManagerByUser(User user) {
		return pickUpPointManagerRepository.findByUser(user);
	}
}
