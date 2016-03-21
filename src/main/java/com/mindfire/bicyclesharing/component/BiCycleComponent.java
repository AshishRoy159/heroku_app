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

import com.mindfire.bicyclesharing.dto.BiCycleDTO;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.service.BiCycleService;
import com.mindfire.bicyclesharing.service.PickUpPointService;

/**
 * BiCycleComponent class is used to get the data from the BiCycleDTO class and
 * set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class BiCycleComponent {

	@Autowired
	private BiCycleService biCycleService;

	@Autowired
	private PickUpPointService pickUpPointService;

	/**
	 * This method is used for receiving the data from BiCycleDTO object and set
	 * the data to the corresponding entity class
	 * 
	 * @param biCycleDTO
	 * @return BiCycle object
	 */
	public BiCycle mapBiCycleData(BiCycleDTO biCycleDTO) {
		BiCycle biCycle = new BiCycle();
		PickUpPoint pickUpPoint = pickUpPointService.getPickupPointById(biCycleDTO.getPickUpPoint());
		biCycle.setChasisNo(biCycleDTO.getChasisNo());
		biCycle.setCurrentLocation(pickUpPoint);
		pickUpPointService.updateBicycleCurrentAvailability(pickUpPoint.getCurrentAvailability() + 1,
				pickUpPoint.getPickUpPointId());
		
		return biCycleService.saveBiCycle(biCycle);

	}

}
