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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.ManageRoleDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.PickUpPointManager;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;

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

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

	@Autowired
	private UserRepository userRepository;

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

	/**
	 * This method receives data from the ManageRoleDTO class and sets data to
	 * the PickUpPointManager entity class
	 * 
	 * @param manageRoleDTO
	 *            the data from the view
	 * @return PickUpPointManager object
	 */
	public PickUpPointManager mapPickUpPointManagerDetails(ManageRoleDTO manageRoleDTO) {
		PickUpPointManager pickUpPointManager = new PickUpPointManager();
		pickUpPointManager.setPickUpPoint(pickUpPointRepository.findByPickUpPointId(manageRoleDTO.getPickUpPointId()));
		pickUpPointManager.setRole(userRepository.findByUserId(manageRoleDTO.getUserId()).getRole());
		pickUpPointManager.setUser(userRepository.findByUserId(manageRoleDTO.getUserId()));

		try {
			logger.info("Added new pickup point manager details to the database.");
			return pickUpPointManagerRepository.save(pickUpPointManager);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used for retrieving the current availability of the pickup
	 * point.
	 * 
	 * @param user
	 *            User object
	 * @return {@link Integer} value
	 */
	public int currentAvailabilityAtPickupPoint(User user) {
		return pickUpPointManagerRepository.findByUser(user).getPickUpPoint().getCurrentAvailability();
	}

	/**
	 * This method is used to map the pickup point details to open the pickup
	 * point when manager is logged In.
	 * 
	 * @param user
	 *            User object
	 * @return {@link PickUpPointManager} object
	 */
	public PickUpPointManager mapPickupPointDetailForOpen(User user) {
		PickUpPointManager pickUpPointManager = pickUpPointManagerRepository.findByUser(user);

		pickUpPointManager.getPickUpPoint().setIsOpen(true);

		try {
			logger.info("Updated pickup point status to open.");
			return pickUpPointManagerRepository.save(pickUpPointManager);
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to map the pickup point details to close the pickup
	 * point when manager is logged In.
	 * 
	 * @param user
	 *            User object
	 * @return {@link PickUpPointManager} object
	 */
	public PickUpPointManager mapPickupPointDetailForClose(User user) {
		PickUpPointManager pickUpPointManager = pickUpPointManagerRepository.findByUser(user);
		pickUpPointManager.getPickUpPoint().setIsOpen(false);

		try {
			logger.info("Updated pickup point status to close.");
			return pickUpPointManagerRepository.save(pickUpPointManager);
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}
}
