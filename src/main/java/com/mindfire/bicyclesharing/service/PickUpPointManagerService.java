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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.PickUpPointManagerComponent;
import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.model.User;

/**
 * PickUpPointManagerService class contains methods for PickUp Point Manager
 * related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class PickUpPointManagerService {

	@Autowired
	private PickUpPointManagerComponent pickUpPointManagerComponent;

	/**
	 * This method is used to save the pickup point manager related data to the
	 * database
	 * 
	 * @param manageRoleDTO
	 *            user role related data.
	 * @return PickUpPointManager object
	 */
	public PickUpPointManager setPickUpPointToManager(ManageRoleDTO manageRoleDTO) {
		return pickUpPointManagerComponent.mapPickUpPointManagerDetails(manageRoleDTO);
	}

	/**
	 * This method used for getting the pickup point manager.
	 * 
	 * @param user
	 *            this is User object
	 * @return {@link PickUpPointManager}
	 */
	public PickUpPointManager getPickupPointManager(User user) {
		return pickUpPointManagerComponent.getPickUpPointManagerByUser(user);
	}

	/**
	 * This method is used to get the current availability of bicycles at a
	 * pickup point
	 * 
	 * @param user
	 *            {@link User} details
	 * @return {@link Integer} current availability amount
	 */
	public int getCurrentAvailability(User user) {
		return pickUpPointManagerComponent.currentAvailabilityAtPickupPoint(user);
	}

	/**
	 * This method is used to get all pickup point manager records
	 * 
	 * @return {@link PickUpPointManager} List
	 */
	public List<PickUpPointManager> getAllPickUpPointManager() {
		return pickUpPointManagerComponent.getAllManager();
	}

	/**
	 * This method is used to open the pickup point when manager is logged In.
	 * 
	 * @param user
	 *            User object
	 * @return {@link PickUpPointManager} object
	 */
	public PickUpPointManager openPickUpPoint(User user) {
		return pickUpPointManagerComponent.mapPickupPointDetailForOpen(user);
	}

	/**
	 * This method is used to close the pickup point when manager is logged In.
	 * 
	 * @param user
	 *            User object
	 * @return {@link PickUpPointManager} object
	 */
	public PickUpPointManager closePickUpPoint(User user) {
		return pickUpPointManagerComponent.mapPickupPointDetailForClose(user);
	}
}
