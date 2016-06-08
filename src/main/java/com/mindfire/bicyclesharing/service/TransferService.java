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

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.BiCycleComponent;
import com.mindfire.bicyclesharing.component.PickUpPointComponent;
import com.mindfire.bicyclesharing.component.TransferComponent;
import com.mindfire.bicyclesharing.dto.TransferDataDTO;
import com.mindfire.bicyclesharing.enums.TransferStatusEnum;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.security.CurrentUser;

/**
 * TransferService class contains methods for Transfer related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class TransferService {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private TransferComponent transferComponent;

	@Autowired
	private PickUpPointManagerService pickUpPointManagerService;

	@Autowired
	private BiCycleComponent biCycleComponent;

	@Autowired
	private PickUpPointComponent pickUpPointComponent;

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

	/**
	 * This method is used to retrieve details of transfers from a pickup point.
	 * 
	 * @param currentUser
	 *            the current logged in manager
	 * @return {@link Transfer} List
	 */
	public List<Transfer> findOutgoingTransfers(CurrentUser currentUser) {
		PickUpPoint pickUpPoint = pickUpPointManagerService.getPickupPointManager(currentUser.getUser())
				.getPickUpPoint();
		return transferComponent.getOutgoingTransfers(pickUpPoint);
	}

	/**
	 * This method is used to retrieve details of transfers to a pickup point.
	 * 
	 * @param currentUser
	 *            the current logged in manager
	 * @return {@link Transfer} List
	 */
	public List<Transfer> findIncomingTransfers(CurrentUser currentUser) {
		PickUpPoint pickUpPoint = pickUpPointManagerService.getPickupPointManager(currentUser.getUser())
				.getPickUpPoint();
		return transferComponent.getIncomingTransfers(pickUpPoint);
	}

	/**
	 * This method is retrieve the transfer details from its id
	 * 
	 * @param transferId
	 *            id of the transfer
	 * @return {@link Transfer} object
	 */
	public Transfer findTransferDetails(Long transferId) {
		return transferComponent.getTransferDetails(transferId);
	}

	/**
	 * This method is used to get all closed transfers.
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link Transfer} {@link DataTablesOutput}
	 */
	public DataTablesOutput<Transfer> findAllClosedTransfers(DataTablesInput input) {
		return transferComponent.getAllClosedTransfers(input);
	}

	/**
	 * This method is used to update the transfer details and details of
	 * bicycles involved on confirmation from dispatcher pickup point.
	 * 
	 * @param transferDataDTO
	 *            the incoming transfer details
	 * @param session
	 *            to get the list of bicycles to be transferred
	 * @return {@link Transfer} object
	 */
	public Transfer confirmTransfer(TransferDataDTO transferDataDTO, HttpSession session) {
		Transfer transfer = transferComponent.updateTransferDetails(transferDataDTO);

		if (transfer == null) {
			logger.error("Data could not be updated.");
			return null;
		} else {
			logger.info("Transfer status is set to 'in transition'.");
			biCycleComponent.bicyclesInTransition(session, transfer);
			PickUpPoint pickUpPoint = transfer.getTransferredFrom();
			pickUpPoint.setCurrentAvailability(biCycleComponent.findByCurrentLocationAndIsAvailable(pickUpPoint, true));
			pickUpPointComponent.updatePickupPoint(pickUpPoint);
			return transfer;
		}
	}

	/**
	 * This method is used to update the transfer details and details of
	 * bicycles involved on confirmation from receiving pickup point.
	 * 
	 * @param transferId
	 *            id of the transfer record
	 * @param session
	 *            to get the list of bicycles transferred
	 * @return {@link Transfer} object
	 */
	public Transfer confirmReceiveTransfer(Long transferId, HttpSession session) {
		Transfer transfer = findTransferDetails(transferId);
		transfer.setArrivedOn(new Timestamp(System.currentTimeMillis()));
		transfer.setStatus(TransferStatusEnum.CLOSED);

		Transfer closedTransfer = transferComponent.closeTransfer(transfer);

		if (closedTransfer == null) {
			logger.error("Data could not be updated.");
			return null;
		} else {
			logger.info("Transfer status is set to 'closed'.");
			biCycleComponent.bicyclesTransferred(session, closedTransfer);
			PickUpPoint pickUpPoint = transfer.getTransferredTo();
			pickUpPoint.setCurrentAvailability(biCycleComponent.findByCurrentLocationAndIsAvailable(pickUpPoint, true));
			pickUpPointComponent.updatePickupPoint(pickUpPoint);
			return closedTransfer;
		}
	}
}
