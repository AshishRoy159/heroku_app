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

import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;

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
	private PickUpPointRepository pickUpPointRepository;

	/**
	 * This method is used to save the pickup point related data to the database
	 * 
	 * @param pickUpPoint
	 * @return PickUpPoint object
	 */
	public PickUpPoint savePickUpPoint(PickUpPoint pickUpPoint) {
		PickUpPoint addedPickUpPoint = pickUpPointRepository.save(pickUpPoint);
		return addedPickUpPoint;
	}

	/**
	 * This method is used to find all pickup points
	 * 
	 * @return PickUpPoint list
	 */
	public List<PickUpPoint> getAllPickupPoints() {
		return pickUpPointRepository.findAllByOrderByPickUpPointIdAsc();
	}

	/**
	 * This method is used to find pickup point by its id
	 * 
	 * @return PickUpPoint object
	 */
	public PickUpPoint getPickupPointById(int pickUpPointId) {
		return pickUpPointRepository.findByPickUpPointId(pickUpPointId);
	}

	/**
	 * This method is used to update pickup point details.
	 * 
	 * @param pickUpPoint
	 * @return Integer either 0 or 1
	 */
	public int updatePickUpPointDetails(PickUpPoint pickUpPoint) {
		return pickUpPointRepository.updatePickUpPoint(pickUpPoint.getLocation(), pickUpPoint.getMaxCapacity(),
				pickUpPoint.getIsActive(), pickUpPoint.getPickUpPointId());
	}
	
	/**
	 * 
	 * @param pickUpPointId
	 * @return
	 */
	public int updateBicycleCurrentAvailability(int currentAvailability, int pickUpPointId){
		return pickUpPointRepository.updateCurrentAvailability(currentAvailability, pickUpPointId);
	}
}
