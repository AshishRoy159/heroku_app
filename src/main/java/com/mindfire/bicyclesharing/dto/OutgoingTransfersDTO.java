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

package com.mindfire.bicyclesharing.dto;

import java.sql.Timestamp;

/**
 * OutgoingTransfersDTO class is used for taking data from the Outgoing
 * Transfers view
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */

public class OutgoingTransfersDTO {

	private String transferredTo;

	private Integer quantity;

	private Timestamp dispatchedAt;

	private String vehicleNo;

	/**
	 * returns the pick up point id where the transfer is being carried to.
	 * @return transferredTo.
	 */
	public String getTransferredTo() {
		return transferredTo;
	}

	/**
	 * sets the pick up point id where the transfer is being carried to.
	 * @param transferredTo
	 */
	public void setTransferredTo(String transferredTo) {
		this.transferredTo = transferredTo;
	}

	/**
	 * returns the quantity of the bicycle transfers.
	 * @return quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * sets the quantity of the bicycle being transferred.
	 * @param quantity
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * returns the time when the dispatch was made.
	 * @return dispatchedAt
	 */
	public Timestamp getDispatchedAt() {
		return dispatchedAt;
	}

	/**
	 * sets the dispatch time of the transfer.
	 * @param dispatchedAt
	 */
	public void setDispatchedAt(Timestamp dispatchedAt) {
		this.dispatchedAt = dispatchedAt;
	}

	/**
	 * returns the vechicle no of the transport.
	 * @return vehicleNo
	 */
	public String getVehicleNo() {
		return vehicleNo;
	}

	/**
	 * sets the vehicle no of the transport.
	 * @param vehicleNo
	 */
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

}
