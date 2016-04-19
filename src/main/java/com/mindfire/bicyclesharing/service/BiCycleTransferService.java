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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.BicycleTransferComponent;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.BiCycleTransfer;
import com.mindfire.bicyclesharing.model.Transfer;

/**
 * BiCycleTransferService class contains methods for BiCycle Transfer related
 * operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class BiCycleTransferService {

	@Autowired
	private BicycleTransferComponent bicycleTransferComponent;

	/**
	 * This method is used to get the list of bicycles that are transferred
	 * 
	 * @param transfer
	 *            concerned transfer record
	 * @return {@link BiCycle} List
	 */
	public List<BiCycle> findBicyclesInTransition(Transfer transfer) {
		List<BiCycleTransfer> biCycleTransfers = bicycleTransferComponent.receiveTransferredBicycles(transfer);
		List<BiCycle> biCycles = new ArrayList<BiCycle>();

		for (BiCycleTransfer biCycleTransfer : biCycleTransfers) {
			BiCycle biCycle = biCycleTransfer.getBiCycle();
			biCycles.add(biCycle);
		}
		return biCycles;
	}
}
