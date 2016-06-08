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
	 * @return the transferredTo
	 */
	public String getTransferredTo() {
		return transferredTo;
	}

	/**
	 * @param transferredTo
	 *            the transferredTo to set
	 */
	public void setTransferredTo(String transferredTo) {
		this.transferredTo = transferredTo;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the dispatchedAt
	 */
	public Timestamp getDispatchedAt() {
		return dispatchedAt;
	}

	/**
	 * @param dispatchedAt
	 *            the dispatchedAt to set
	 */
	public void setDispatchedAt(Timestamp dispatchedAt) {
		this.dispatchedAt = dispatchedAt;
	}

	/**
	 * @return the vehicleNo
	 */
	public String getVehicleNo() {
		return vehicleNo;
	}

	/**
	 * @param vehicleNo
	 *            the vehicleNo to set
	 */
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
}
