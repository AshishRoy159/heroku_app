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

import com.mindfire.bicyclesharing.dto.PickUpPointDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.service.PickUpPointService;

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
	private PickUpPointService pickUpPointService;

	/**
	 * This method is used for receiving the data from PickUpPointDto object and
	 * set the data to the corresponding entity class
	 * 
	 * @param pickUpPointDto
	 * @return PickUpPoint object
	 */
	public PickUpPoint mapPickUpPointDetails(PickUpPointDTO pickUpPointDTO) {
		PickUpPoint pickUpPoint = new PickUpPoint();

		pickUpPoint.setLocation(pickUpPointDTO.getLocation());
		pickUpPoint.setMaxCapacity(pickUpPointDTO.getMaxCapacity());
		pickUpPoint.setCurrentAvailability(0);

		return pickUpPointService.savePickUpPoint(pickUpPoint);
	}
}
