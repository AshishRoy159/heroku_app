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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.TransferComponent;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.model.TransferResponse;

/**
 * TransferService class contains methods for Transfer related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class TransferService {

	@Autowired
	private TransferComponent transferComponent;

	/**
	 * This method is used to add new transfer entry to the database.
	 * 
	 * @param transferResponse
	 *            the approved transfer respnse data
	 * @return {@link Transfer} object
	 */
	public Transfer addNewTransfer(TransferResponse transferResponse) {
		return transferComponent.mapNewTransfer(transferResponse);
	}
}
