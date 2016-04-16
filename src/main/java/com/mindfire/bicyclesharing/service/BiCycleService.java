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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.BiCycleComponent;
import com.mindfire.bicyclesharing.dto.BiCycleDTO;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Transfer;

import javassist.NotFoundException;

/**
 * BiCycleService class contains methods for BiCycle related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class BiCycleService {

	@Autowired
	private BiCycleComponent biCycleComponent;

	/**
	 * This method is used for updating the bicycle availability.
	 * 
	 * @param id
	 *            this is biCycleId
	 * @return {@link BiCycle} object
	 * @throws NotFoundException 
	 */
	public BiCycle updateBiCycleDetails(Long id) throws NotFoundException {
		return biCycleComponent.updateBicycle(id);
	}

	/**
	 * This method is used to retrieve a certain number of bicycles based on
	 * current location and availability.
	 * 
	 * @param transfer
	 *            concerned transfer record
	 * @return {@link BiCycle} List
	 * @throws NotFoundException 
	 */
	public List<BiCycle> findBicyclesForShipment(Transfer transfer) throws NotFoundException {
		Pageable pageable = new PageRequest(0, transfer.getQuantity());
		return biCycleComponent.getAvailableBicycles(transfer.getTransferredFrom(), true, pageable);
	}

	/**
	 * This method is used to retrieve bicycles based on current location and
	 * availability.
	 * 
	 * @param pickUpPoint
	 *            current pickup point
	 * 
	 * @param availability
	 *            availability of bicycle
	 * 
	 * @return {@link BiCycle} List
	 * @throws NotFoundException 
	 */
	public List<BiCycle> getBicyclesByPickupPointAndAvailability(PickUpPoint pickUpPoint, Boolean availability) throws NotFoundException {
		return biCycleComponent.findByCurrentLocationAndAvailability(pickUpPoint, availability);
	}

	/**
	 * 
	 * @param biCycleDTO
	 * @return
	 * @throws NotFoundException 
	 */
	public BiCycle saveBiCycleDetails(BiCycleDTO biCycleDTO) throws NotFoundException {
		return biCycleComponent.mapBiCycleData(biCycleDTO);
	}
}
