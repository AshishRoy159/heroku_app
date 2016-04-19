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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.TransferRequestDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.TransferRequestRepository;
import com.mindfire.bicyclesharing.security.CurrentUser;

/**
 * TransferRequestComponent class is used to get the data from the
 * TransferRequestDTO class and set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class TransferRequestComponent {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private TransferRequestRepository transferRequestRepository;

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	/**
	 * This method is used to receive data from the TransferRequestDTO class and
	 * set the data to the corresponding entity class
	 * 
	 * @param authentication
	 *            to retrieve the current user
	 * @param transferRequestDTO
	 *            the data to be set to the entity class
	 * @return TransferRequest object
	 */
	public TransferRequest mapNewRequest(Authentication authentication, TransferRequestDTO transferRequestDTO) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		PickUpPoint pickUpPoint = pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint();

		if (transferRequestDTO.getQuantity() > (pickUpPoint.getMaxCapacity() - pickUpPoint.getCurrentAvailability())) {
			logger.info("Requested transfer quantity is more than remaining space at pickup point.");
			return null;
		}
		TransferRequest transferRequest = new TransferRequest();

		transferRequest.setPickUpPoint(pickUpPoint);
		transferRequest.setManager(currentUser.getUser());
		transferRequest.setQuantity(transferRequestDTO.getQuantity());

		try {
			logger.info("New transfer request created.");
			return transferRequestRepository.save(transferRequest);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to retrieve all transfer requests from the database
	 * 
	 * @return {@link TransferRequest} List
	 */
	public List<TransferRequest> getAllRequests() {
		return transferRequestRepository.findAll();
	}

	/**
	 * This method is used to retrieve transfer requests from other pickup
	 * points
	 * 
	 * @param currentUser
	 *            the current logged in manager
	 * @return {@link TransferRequest} List
	 */
	public List<TransferRequest> getOthersRequest(CurrentUser currentUser) {
		PickUpPoint pickUpPoint = pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint();
		return transferRequestRepository.findByPickUpPointNot(pickUpPoint);
	}

	/**
	 * This method is used to retrieve the transfer request record from
	 * requestId
	 * 
	 * @param requestId
	 *            the id of the request object
	 * @return {@link TransferRequest} object
	 */
	public TransferRequest getTransferRequest(Long requestId) {
		return transferRequestRepository.findByRequestId(requestId);
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
	public int updateApprovedQuantity(Integer approvedQuantity, Long requestId) {
		try {
			logger.info("Updated approved quantity for the request.");
			return transferRequestRepository.updateCurrentApprovedQuantity(approvedQuantity, requestId);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to update the approval of the current transfer
	 * request
	 * 
	 * @param isApproved
	 *            approval status. <code>true</code> or <code>false</code>
	 * @param requestId
	 *            id of the request
	 * @return {@link Integer} 0 or 1
	 */
	public int uppdateIsApproved(Boolean isApproved, Long requestId) {
		try {
			logger.info("Updated ststus of the request.");
			return transferRequestRepository.updateIsApproved(isApproved, requestId);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}
}
