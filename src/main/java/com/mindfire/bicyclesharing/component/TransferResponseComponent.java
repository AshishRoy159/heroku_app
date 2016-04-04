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

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.TransferRensponseDTO;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.TransferRequestRepository;
import com.mindfire.bicyclesharing.repository.TransferResponseRepository;

/**
 * TransferResponseComponent class is used to get the data from the
 * TransferRensponseDTO class and set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class TransferResponseComponent {

	@Autowired
	private TransferResponseRepository transferResponseRepository;

	@Autowired
	private TransferRequestRepository transferRequestRepository;

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	/**
	 * This method is used to save a response from one pickup point to transfer
	 * requests from another pickup point
	 * 
	 * @param transferRensponseDTO
	 *            the incoming response details
	 * @param currentUser
	 *            the current logged in manager
	 * @return {@link TransferResponse} object
	 */
	public TransferResponse mapNewTransferResponse(TransferRensponseDTO transferRensponseDTO, CurrentUser currentUser) {

		TransferResponse transferResponse = new TransferResponse();
		transferResponse.setRequest(transferRequestRepository.findByRequestId(transferRensponseDTO.getRequestId()));
		transferResponse.setQuantity(transferRensponseDTO.getQuantity());

		transferResponse
				.setPickUpPoint(pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint());
		transferResponse.setManager(currentUser.getUser());

		return transferResponseRepository.save(transferResponse);
	}

	/**
	 * This method is used to retrieve responses from the current pickup point
	 * 
	 * @param currentUser
	 *            the current logged in user
	 * @return {@link TransferResponse} List
	 */
	public List<TransferResponse> getResponses(CurrentUser currentUser) {
		PickUpPoint pickUpPoint = pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint();
		return transferResponseRepository.findByPickUpPoint(pickUpPoint);
	}

	/**
	 * This method is used to retrieve responses for a specific request.
	 * 
	 * @param transferRequest
	 *            the transfer request
	 * @return {@link TransferResponse} List
	 */
	public List<TransferResponse> getTransferResponsesForRequest(TransferRequest transferRequest) {
		return transferResponseRepository.findByRequest(transferRequest);
	}

	/**
	 * This method is used to retrieve details of a transfer response from its
	 * id.
	 * 
	 * @param responseId
	 *            the id of the transfer response
	 * @return {@link TransferResponse} object
	 */
	public TransferResponse getTransferResponse(Long responseId) {
		return transferResponseRepository.findByResponseId(responseId);
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
	public int updateIsApproved(Boolean isApproved, Long responseId) {
		return transferResponseRepository.updateIsApproved(isApproved, responseId);
	}

}
