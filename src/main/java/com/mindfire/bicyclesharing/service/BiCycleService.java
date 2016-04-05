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
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;

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
	private BiCycleRepository biCycleRepository;

	@Autowired
	private BiCycleComponent biCycleComponent;

	/**
	 * This method is used for saving the bicycle details.
	 * 
	 * @param biCycle
	 *            the BiCycle object to be stored in database
	 * @return the stored BiCycle object
	 */
	public BiCycle saveBiCycle(BiCycle biCycle) {
		return biCycleRepository.save(biCycle);
	}

	/**
	 * This method is used for finding all bicycle at any particular pickup
	 * point.
	 * 
	 * @param pickUpPointId
	 *            the id of the respective pickup point
	 * @return BiCycle list
	 */
	public List<BiCycle> findAllBiCycleByPickUpPointId(PickUpPoint pickUpPointId) {
		return biCycleRepository.findAllByCurrentLocation(pickUpPointId);
	}

	/**
	 * This method is used for updating the bicycle availability.
	 * 
	 * @param id
	 *            this is biCycleId
	 * @return BiCycle object
	 */
	public BiCycle updateBiCycleDetails(Long id) {
		BiCycle biCycle = biCycleRepository.findByBiCycleId(id);
		biCycle.setIsAvailable(false);
		biCycleRepository.save(biCycle);

		return biCycle;
	}

	/**
	 * 
	 * @param transfer
	 * @return
	 */
	public List<BiCycle> findBicyclesForShipment(Transfer transfer) {
		Pageable pageable = new PageRequest(0, transfer.getQuantity());
		return biCycleComponent.getAvailableBicycles(transfer.getTransferredFrom(), true, pageable);
	}
}
