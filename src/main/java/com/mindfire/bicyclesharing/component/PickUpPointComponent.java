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

import com.mindfire.bicyclesharing.dto.PickUpPointDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;

/**
 * PickUpPointComponent class is used to get the data from the PickUpPointDto
 * class and set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class PickUpPointComponent {

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

	/**
	 * This method is used for receiving the data from PickUpPointDto object and
	 * set the data to the corresponding entity class
	 * 
	 * @param pickUpPointDTO
	 *            the data from the view
	 * @return PickUpPoint object
	 */
	public PickUpPoint mapPickUpPointDetails(PickUpPointDTO pickUpPointDTO) {
		PickUpPoint pickUpPoint = new PickUpPoint();

		pickUpPoint.setLocation(pickUpPointDTO.getLocation());
		pickUpPoint.setMaxCapacity(pickUpPointDTO.getMaxCapacity());
		pickUpPoint.setCurrentAvailability(0);

		return pickUpPointRepository.save(pickUpPoint);
	}

	/**
	 * This method is used for receiving the data from PickUpPointDto object and
	 * set the data to the corresponding entity class
	 * 
	 * @param pickUpPointDTO
	 *            the data from the view
	 * @return {@link PickUpPoint} object
	 */
	public PickUpPoint mapUpdatePickUpPointDetails(PickUpPointDTO pickUpPointDTO) {
		PickUpPoint pickUpPoint = pickUpPointRepository.findByPickUpPointId(pickUpPointDTO.getPickUpPointId());

		pickUpPoint.setLocation(pickUpPointDTO.getLocation());
		pickUpPoint.setMaxCapacity(pickUpPointDTO.getMaxCapacity());
		pickUpPoint.setIsActive(pickUpPointDTO.getIsActive());

		return pickUpPointRepository.save(pickUpPoint);
	}

	/**
	 * This method is used to update the details of an existing PickUpPoint
	 * 
	 * @param pickUpPoint
	 *            the pickup point to be updated
	 * @return {@link PickUpPoint} object
	 */
	public PickUpPoint updatePickupPoint(PickUpPoint pickUpPoint) {
		return pickUpPointRepository.save(pickUpPoint);
	}

	/**
	 * This method is used to find all PickupPoint
	 * 
	 * @return {@link PickUpPoint} List
	 */
	public List<PickUpPoint> findAllPickUpPoint() {
		return pickUpPointRepository.findAllByOrderByPickUpPointIdAsc();
	}

	/**
	 * This method is used to find all active PickupPoint
	 * 
	 * @param isActive
	 *            true or false
	 * @return {@link PickUpPoint} List
	 */
	public List<PickUpPoint> findAllActivePickUpPoint(Boolean isActive) {
		return pickUpPointRepository.findByIsActiveOrderByPickUpPointIdAsc(isActive);
	}

	/**
	 * This method is used to update the current availability.
	 * 
	 * @param pickUpPoint
	 *            PickUpPoint object
	 * @param size
	 *            number of bicycle
	 * @return {@link PickUpPoint} object
	 */
	public PickUpPoint updateBiCycleCurrentAvailability(PickUpPoint pickUpPoint, int size) {
		pickUpPoint.setCurrentAvailability(size);
		return pickUpPointRepository.save(pickUpPoint);
	}

	/**
	 * This method is used to find the PickUpPoint details by id
	 * 
	 * @param id
	 *            pickUpPoint id
	 * @return {@link PickUpPoint} object
	 */
	public PickUpPoint findPickUpPointById(int id) {
		return pickUpPointRepository.findByPickUpPointId(id);
	}
}
