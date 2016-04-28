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
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.TransferRequestComponent;
import com.mindfire.bicyclesharing.component.TransferResponseComponent;
import com.mindfire.bicyclesharing.dto.TransferRequestDTO;
import com.mindfire.bicyclesharing.dto.TransferRequestRespondedDTO;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.security.CurrentUser;

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

	@Autowired
	private TransferResponseComponent transferResponseComponent;

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

	/**
	 * This method is used to retrieve details all transfer requests'
	 * 
	 * @return {@link TransferRequest} List
	 */
	public List<TransferRequest> findAllRequests() {
		return transferRequestComponent.getAllRequests();
	}

	/**
	 * This method is used to retrieve transfer request details from other
	 * pickup points.
	 * 
	 * @param currentUser
	 *            to get the current logged in manager details
	 * @param isApproved
	 *            status of the request
	 * @return {@link TransferRequestRespondedDTO} List
	 */
	public List<TransferRequestRespondedDTO> findOtherRequest(CurrentUser currentUser, Boolean isApproved) {
		List<TransferRequest> requests = transferRequestComponent.getOthersRequest(currentUser, isApproved);
		List<TransferResponse> responses = transferResponseComponent.getResponses(currentUser);
		return setIsRespondedOrNot(requests, responses);
	}

	/**
	 * This method is used to retrieve details of transfer request from its id.
	 * 
	 * @param requestId
	 *            the id of the transfer request
	 * @return {@link TransferRequest} object
	 */
	public TransferRequest findTransferRequest(Long requestId) {
		return transferRequestComponent.getTransferRequest(requestId);
	}

	/**
	 * This method is used to update the approved quantity field of the request.
	 * 
	 * @param approvedQuantity
	 *            the updated amount
	 * @param requestId
	 *            the id of the request
	 * @return Integer 0 or 1
	 */
	public int updateQuantityApproved(Integer approvedQuantity, Long requestId) {
		return transferRequestComponent.updateApprovedQuantity(approvedQuantity, requestId);
	}

	/**
	 * This method is used to retrieve transfer requests by approval status
	 * 
	 * @param isApproved
	 *            approval status
	 * @return {@link TransferRequest} List
	 */
	public List<TransferRequest> findRequestsByApproval(Boolean isApproved) {
		return transferRequestComponent.getRequestsByApproval(isApproved);
	}

	/**
	 * This method is used to update approval status of the transfer request
	 * 
	 * @param transferRequest
	 *            the concerned request
	 * @return {@link TransferRequest} object
	 */
	public TransferRequest updateAppproval(TransferRequest transferRequest) {
		return transferRequestComponent.setApproved(transferRequest);
	}

	/**
	 * This method is used to get all approved transfer requests
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link TransferRequest} {@link DataTablesOutput}
	 */
	public DataTablesOutput<TransferRequest> findAllClosedRequests(DataTablesInput input) {
		return transferRequestComponent.getAllClosedRequests(input);
	}

	/**
	 * This method is used to check if the transfer requests are responded by
	 * current pickup point or not
	 * 
	 * @param requests
	 *            the transfer requests from other pickup points
	 * @param responses
	 *            the responses from current pickup point
	 * @return {@link TransferRequestRespondedDTO} List
	 */
	public List<TransferRequestRespondedDTO> setIsRespondedOrNot(List<TransferRequest> requests,
			List<TransferResponse> responses) {
		List<TransferRequestRespondedDTO> allRequests = new ArrayList<TransferRequestRespondedDTO>();

		for (TransferRequest request : requests) {
			TransferRequestRespondedDTO transferRequestRespondedDTO = new TransferRequestRespondedDTO();

			transferRequestRespondedDTO.setRequestId(request.getRequestId());
			transferRequestRespondedDTO.setPickUpPoint(request.getPickUpPoint());
			transferRequestRespondedDTO.setManager(request.getManager());
			transferRequestRespondedDTO.setQuantity(request.getQuantity());
			transferRequestRespondedDTO.setRequestedOn(request.getRequestedOn());
			transferRequestRespondedDTO.setIsApproved(request.getIsApproved());
			transferRequestRespondedDTO.setIsResponded(false);

			for (TransferResponse response : responses) {
				if (response.getRequest().getRequestId() == request.getRequestId()) {
					transferRequestRespondedDTO.setIsResponded(true);
					break;
				}
			}
			allRequests.add(transferRequestRespondedDTO);
		}
		return allRequests;
	}
}
