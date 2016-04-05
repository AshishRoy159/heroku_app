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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.TransferDataDTO;
import com.mindfire.bicyclesharing.enums.TransferStatusEnum;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.model.TransferResponse;
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
		int updatedApprovedQuantity;

		if (transferResponse.getQuantity() >= (transferResponse.getRequest().getQuantity()
				- transferResponse.getRequest().getApprovedQuantity())) {
			transfer.setQuantity(
					transferResponse.getRequest().getQuantity() - transferResponse.getRequest().getApprovedQuantity());
		} else {
			transfer.setQuantity(transferResponse.getQuantity());
		}

		updatedApprovedQuantity = transferResponse.getRequest().getApprovedQuantity() + transfer.getQuantity();

		transferRequestComponent.updateApprovedQuantity(updatedApprovedQuantity,
				transferResponse.getRequest().getRequestId());

		if (transferResponse.getRequest().getQuantity() == updatedApprovedQuantity) {
			transferRequestComponent.uppdateIsApproved(true, transferResponse.getRequest().getRequestId());
		}

		transfer.setTransferredFrom(transferResponse.getPickUpPoint());
		transfer.setTransferredTo(transferResponse.getRequest().getPickUpPoint());
		transfer.setStatus(TransferStatusEnum.PENDING);

		return transferRepository.save(transfer);
	}

	/**
	 * 
	 * @param pickUpPoint
	 * @return
	 */
	public List<Transfer> getOutgoingTransfers(PickUpPoint pickUpPoint) {
		return transferRepository.findByTransferredFrom(pickUpPoint);
	}

	/**
	 * 
	 * @param pickUpPoint
	 * @return
	 */
	public List<Transfer> getIncomingTransfers(PickUpPoint pickUpPoint) {
		return transferRepository.findByTransferredTo(pickUpPoint);
	}

	/**
	 * 
	 * @param transferId
	 * @return
	 */
	public Transfer getTransferDetails(Long transferId) {
		return transferRepository.findByTransferId(transferId);
	}

	/**
	 * 
	 * @param transferDataDTO
	 * @return
	 */
	public Transfer updateTransferDetails(TransferDataDTO transferDataDTO) {
		Transfer transfer = getTransferDetails(transferDataDTO.getTransferId());
		transfer.setDispatchedAt(new Timestamp(System.currentTimeMillis()));
		transfer.setVehicleNo(transferDataDTO.getVehicleNo());
		transfer.setStatus(TransferStatusEnum.IN_TRANSITION);

		return transferRepository.save(transfer);
	}

}
