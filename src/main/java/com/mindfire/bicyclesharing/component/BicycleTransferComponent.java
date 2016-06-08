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

import com.mindfire.bicyclesharing.model.BiCycleTransfer;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.repository.BiCycleTransferRepository;

/**
 * BicycleTransferComponent class is used to interact with the corresponding
 * repository.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class BicycleTransferComponent {

	@Autowired
	private BiCycleTransferRepository biCycleTransferRepository;

	/**
	 * This method is used to retrieve the data of bicycles of a particular
	 * transfer
	 * 
	 * @param transfer
	 *            the transfer for which data is retrieved
	 * @return {@link BiCycleTransfer} List
	 */
	public List<BiCycleTransfer> receiveTransferredBicycles(Transfer transfer) {
		return biCycleTransferRepository.findByTransfer(transfer);
	}
}
