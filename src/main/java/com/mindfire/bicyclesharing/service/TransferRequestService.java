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
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.component.TransferRequestComponent;
import com.mindfire.bicyclesharing.component.TransferResponseComponent;
import com.mindfire.bicyclesharing.dto.TransferRequestDTO;
import com.mindfire.bicyclesharing.dto.TransferRequestRespondedDTO;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;

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
	 * 
	 * @return
	 */
	public List<TransferRequest> findAllRequests() {
		return transferRequestComponent.getAllRequests();
	}

	/**
	 * 
	 * @param currentUser
	 * @return
	 */
	public List<TransferRequestRespondedDTO> findOtherRequest(CurrentUser currentUser) {
		List<TransferRequest> requests = transferRequestComponent.getOthersRequest(currentUser);
		List<TransferResponse> responses = transferResponseComponent.getResponses(currentUser);
		return setIsRespondedOrNot(requests, responses);
	}

	/**
	 * 
	 * @param requestId
	 * @return
	 */
	public TransferRequest findTransferRequest(Long requestId) {
		return transferRequestComponent.getTransferRequest(requestId);
	}

	public List<TransferRequestRespondedDTO> setIsRespondedOrNot(List<TransferRequest> requests,
			List<TransferResponse> responses) {
		List<TransferRequestRespondedDTO> allRequests = new ArrayList<TransferRequestRespondedDTO>();
		ListIterator<TransferRequest> requestIterator = requests.listIterator();
		ListIterator<TransferResponse> responseIterator = responses.listIterator();
		Boolean responded = false;
		
		while (requestIterator.hasNext()) {
			TransferRequest transferRequest = requestIterator.next();
			TransferRequestRespondedDTO transferRequestRespondedDTO = new TransferRequestRespondedDTO();
			transferRequestRespondedDTO.setRequestId(transferRequest.getRequestId());
			transferRequestRespondedDTO.setPickUpPoint(transferRequest.getPickUpPoint());
			transferRequestRespondedDTO.setManager(transferRequest.getManager());
			transferRequestRespondedDTO.setQuantity(transferRequest.getQuantity());
			transferRequestRespondedDTO.setRequestedOn(transferRequest.getRequestedOn());
			transferRequestRespondedDTO.setIsApproved(transferRequest.getIsApproved());
			
			while (responseIterator.hasNext()) {
				TransferResponse transferResponse = responseIterator.next();
				if (transferRequest.getRequestId() == transferResponse.getRequest().getRequestId()) {
					responded = true;
					break;
				}
			}
			transferRequestRespondedDTO.setIsResponded(responded);
			allRequests.add(transferRequestRespondedDTO);
		}
		
		return allRequests;
	}
}
