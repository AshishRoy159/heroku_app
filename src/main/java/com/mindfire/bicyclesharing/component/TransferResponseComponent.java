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
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.TransferRensponseDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.TransferRequestRepository;
import com.mindfire.bicyclesharing.repository.TransferResponseRepository;
import com.mindfire.bicyclesharing.security.CurrentUser;

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

	Logger logger = Logger.getLogger(getClass());

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

		try {
			logger.info("New response to a transfer request added.");
			return transferResponseRepository.save(transferResponse);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
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
		try {
			return transferResponseRepository.findByResponseId(responseId);
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
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
		try {
			logger.info("Updated status of the response.");
			return transferResponseRepository.updateIsApproved(isApproved, responseId);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to retrieve record of a transfer response from a
	 * specific pickup point to a specific request.
	 * 
	 * @param transferRequest
	 *            the transfer request
	 * @param pickUpPoint
	 *            the concerned pickup point
	 * @return {@link TransferResponse} null or not
	 */
	public Optional<TransferResponse> getResposneForRequest(TransferRequest transferRequest, PickUpPoint pickUpPoint) {
		return transferResponseRepository.findByRequestAndPickUpPoint(transferRequest, pickUpPoint);
	}
}
