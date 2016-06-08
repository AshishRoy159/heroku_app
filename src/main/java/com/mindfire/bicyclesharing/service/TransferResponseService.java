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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.TransferResponseComponent;
import com.mindfire.bicyclesharing.dto.TransferRensponseDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.security.CurrentUser;

/**
 * TransferResponseService class contains methods for Transfer response related
 * operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class TransferResponseService {

	@Autowired
	private TransferResponseComponent transferResponseComponent;

	/**
	 * This method is used to add new transfer response data to the database
	 * 
	 * @param transferResponseDTO
	 *            the incoming transfer response data
	 * @param currentUser
	 *            the current logged in manager
	 * @return {@link TransferResponse} object
	 */
	public TransferResponse addNewResponse(TransferRensponseDTO transferResponseDTO, CurrentUser currentUser) {
		return transferResponseComponent.mapNewTransferResponse(transferResponseDTO, currentUser);

	}

	/**
	 * This method is used to retrieve responses from current pickup point
	 * 
	 * @param currentUser
	 *            the current logged in user
	 * @return {@link TransferResponse} List
	 */
	public List<TransferResponse> findResponses(CurrentUser currentUser) {
		return transferResponseComponent.getResponses(currentUser);
	}

	/**
	 * This method is used to retrieve all responses for a specific request
	 * 
	 * @param transferRequest
	 *            the transfer request
	 * @return {@link TransferResponse} List
	 */
	public List<TransferResponse> findresponsesForRequest(TransferRequest transferRequest) {
		return transferResponseComponent.getTransferResponsesForRequest(transferRequest);
	}

	/**
	 * This method is used to get response details using response id
	 * 
	 * @param responseId
	 *            the id of the transfer response
	 * @return {@link TransferResponse} object
	 */
	public TransferResponse findResponseById(Long responseId) {
		return transferResponseComponent.getTransferResponse(responseId);
	}

	/**
	 * This method is used to update if the response is approved or not
	 * 
	 * @param isApproved
	 *            the boolean value
	 * @param responseId
	 *            id of the response
	 * @return Integer 0 or 1
	 */
	public int updateApproval(Boolean isApproved, Long responseId) {
		return transferResponseComponent.updateIsApproved(isApproved, responseId);
	}

	/**
	 * This method is used to retrieve record of a transfer response from a
	 * specific pickup point to a specific request.
	 * 
	 * @param transferRequest
	 *            the transfer request
	 * @param pickUpPoint
	 *            the concerned pickup point
	 * @return {@link TransferResponse} object null or not
	 */
	public Optional<TransferResponse> findResponseForRequest(TransferRequest transferRequest, PickUpPoint pickUpPoint) {
		return transferResponseComponent.getResposneForRequest(transferRequest, pickUpPoint);
	}
}
