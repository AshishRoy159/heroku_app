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

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.TransferDataDTO;
import com.mindfire.bicyclesharing.enums.TransferStatusEnum;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.model.TransferResponse;
import com.mindfire.bicyclesharing.model.Transfer_;
import com.mindfire.bicyclesharing.repository.TransferRepository;

/**
 * TransferComponent class is used to set the data to the corresponding Entity
 * class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class TransferComponent {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private TransferRepository transferRepository;

	@Autowired
	private TransferRequestComponent transferRequestComponent;

	/**
	 * This method is used to receive the data from the approved transfer
	 * response and set to the corresponding entity class.
	 * 
	 * @param transferResponse
	 *            the incoming approved transfer data
	 * @return {@link Transfer} object
	 */
	public Transfer mapNewTransfer(TransferResponse transferResponse) {
		Transfer transfer = new Transfer();

		if (transferResponse.getQuantity() >= (transferResponse.getRequest().getQuantity()
				- transferResponse.getRequest().getApprovedQuantity())) {
			logger.info(
					"Response quantity is more than required for request. Transfer quantity is set to required quantity.");
			transfer.setQuantity(
					transferResponse.getRequest().getQuantity() - transferResponse.getRequest().getApprovedQuantity());
		} else {
			logger.info("Transfer quantity is set to required quantity.");
			transfer.setQuantity(transferResponse.getQuantity());
		}
		int updatedApprovedQuantity = transferResponse.getRequest().getApprovedQuantity() + transfer.getQuantity();
		transferRequestComponent.updateApprovedQuantity(updatedApprovedQuantity,
				transferResponse.getRequest().getRequestId());

		if (transferResponse.getRequest().getQuantity() == updatedApprovedQuantity) {
			logger.info("Approved quantity has matched requested quantity. Transfer request is approved now.");
			transferRequestComponent.uppdateIsApproved(true, transferResponse.getRequest().getRequestId());
		}

		transfer.setTransferredFrom(transferResponse.getPickUpPoint());
		transfer.setTransferredTo(transferResponse.getRequest().getPickUpPoint());
		transfer.setStatus(TransferStatusEnum.PENDING);

		try {
			logger.info("New transfer order created.");
			return transferRepository.save(transfer);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to retrieve the details of transfers from a pickup
	 * point.
	 * 
	 * @param pickUpPoint
	 *            concerned pickup point
	 * @return {@link Transfer} List
	 */
	public List<Transfer> getOutgoingTransfers(PickUpPoint pickUpPoint) {
		return transferRepository.findByTransferredFrom(pickUpPoint);
	}

	/**
	 * This method is used to retrieve the details of transfers to a pickup
	 * point.
	 * 
	 * @param pickUpPoint
	 *            concerned pickup point
	 * @return {@link Transfer} List
	 */
	public List<Transfer> getIncomingTransfers(PickUpPoint pickUpPoint) {
		return transferRepository.findByTransferredTo(pickUpPoint);
	}

	/**
	 * This method is used to get the details of a transfer from its id.
	 * 
	 * @param transferId
	 *            id of the transfer
	 * @return {@link Transfer} object
	 */
	public Transfer getTransferDetails(Long transferId) {
		try {
			return transferRepository.findByTransferId(transferId);
		} catch (Exception e) {
			throw new CustomException(ExceptionMessages.NO_DATA_AVAILABLE, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * This method is used to update the transfer details when the dispatcher
	 * pickup point confirms transfer
	 * 
	 * @param transferDataDTO
	 *            the incoming transfer details
	 * @return {@link Transfer} object
	 */
	public Transfer updateTransferDetails(TransferDataDTO transferDataDTO) {
		Transfer transfer = getTransferDetails(transferDataDTO.getTransferId());

		transfer.setDispatchedAt(new Timestamp(System.currentTimeMillis()));
		transfer.setVehicleNo(transferDataDTO.getVehicleNo());
		transfer.setStatus(TransferStatusEnum.IN_TRANSITION);

		try {
			return transferRepository.save(transfer);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to update the transfer details when the receiving
	 * pickup point confirms delivery
	 * 
	 * @param transfer
	 *            the transfer details
	 * @return {@link Transfer} object
	 */
	public Transfer closeTransfer(Transfer transfer) {
		try {
			return transferRepository.save(transfer);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used to get all closed transfers.
	 * 
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link Transfer} {@link DataTablesOutput}
	 */
	public DataTablesOutput<Transfer> getAllClosedTransfers(DataTablesInput input) {
		return transferRepository.findAll(input, getClosedTransferSpecification());
	}

	/**
	 * This method is used to get specification of transfer as closed
	 * 
	 * @return {@link Transfer} {@link Specification}
	 */
	private Specification<Transfer> getClosedTransferSpecification() {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			predicate = criteriaBuilder.and(predicate,
					criteriaBuilder.equal(root.get(Transfer_.status), TransferStatusEnum.CLOSED));
			return predicate;
		};
	}
}
