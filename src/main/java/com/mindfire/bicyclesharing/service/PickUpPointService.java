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

import com.mindfire.bicyclesharing.component.PickUpPointComponent;
import com.mindfire.bicyclesharing.dto.PickUpPointDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;

/**
 * PickUpPointService class contains methods for PickUp Point related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class PickUpPointService {

	@Autowired
	private PickUpPointComponent pickUpPointComponent;

	/**
	 * This method is used to save the pickup point related data to the database
	 * 
	 * @param pickUpPointDTO
	 *            this parameter holds PickUpPoint related data
	 * @return PickUpPoint object
	 */
	public String savePickUpPoint(PickUpPointDTO pickUpPointDTO) {
		return pickUpPointComponent.mapPickUpPointDetails(pickUpPointDTO);
	}

	/**
	 * This method is used to find all pickup points
	 * 
	 * @return PickUpPoint list
	 */
	public List<PickUpPoint> getAllPickupPoints() {
		return pickUpPointComponent.findAllPickUpPoint();
	}
	
	/**
	 * This method is used to find all active pickup points
	 * 
	 * @param isActive true or false
	 * @return PickUpPoint list
	 */
	public List<PickUpPoint> getAllActivePickupPoints(Boolean isActive) {
		return pickUpPointComponent.findAllActivePickUpPoint(isActive);
	}

	/**
	 * This method is used to find pickup point by its id
	 * 
	 * @param pickUpPointId
	 *            the id of the respective pickup point
	 * @return PickUpPoint object
	 */
	public PickUpPoint getPickupPointById(int pickUpPointId) {
		return pickUpPointComponent.findPickUpPointById(pickUpPointId);
	}

	/**
	 * This method is used to update pickup point details.
	 * 
	 * @param pickUpPointDTO
	 *            the PickUpPoint data to be updated
	 * @return Integer either 0 or 1
	 */
	public PickUpPoint updatePickUpPointDetails(PickUpPointDTO pickUpPointDTO) {
		return pickUpPointComponent.mapUpdatePickUpPointDetails(pickUpPointDTO);
	}
	
	/**
	 * This method is to update the currenytAvailability
	 * 
	 * @param pickUpPoint
	 *            PickUpPoint object
	 * @param size
	 *            number of bicycle
	 * @return {@link PickUpPoint} object
	 */
	public PickUpPoint updatePickUpPointAvailability(PickUpPoint pickUpPoint, int size) {
		return pickUpPointComponent.updateBiCycleCurrentAvailability(pickUpPoint, size);
	}
}
