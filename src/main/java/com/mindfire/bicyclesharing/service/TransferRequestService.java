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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.TransferRequestComponent;
import com.mindfire.bicyclesharing.dto.TransferRequestDTO;
import com.mindfire.bicyclesharing.model.TransferRequest;

/**
 * TransferRequestService class contains methods for Transfer Request related
 * operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class TransferRequestService {

	@Autowired
	private TransferRequestComponent transferRequestComponent;

	/**
	 * This method is used to add new transfer request entry to the database.
	 * 
	 * @param authentication
	 *            to retrieve the current user details
	 * @param transferRequestDTO
	 *            the transfer request data
	 * @return TransferRequest object
	 */
	public TransferRequest addNewTransferRequest(Authentication authentication, TransferRequestDTO transferRequestDTO) {
		return transferRequestComponent.mapNewRequest(authentication, transferRequestDTO);
	}

	public Page<TransferRequest> findAllRequests(Integer page) {
		return transferRequestComponent.getAllRequests(page);
	}
}
