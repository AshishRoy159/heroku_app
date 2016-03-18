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
 * IncomingTransfersDTO class is used for taking data from the Incoming
 * Transfers view
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */

public class IncomingTransfersDTO {

	private String transferredFrom;
	private Integer quantity;
	private String vehicleNo;
	private Timestamp arrivedOn;

	/**
	 * returns the pickup pint id from where the transfer is being done.
	 * @return transferredFrom
	 */
	public String getTransferredFrom() {
		return transferredFrom;
	}

	/**
	 * sets the pick up point id from where the transfer is being done.
	 * @param transferredFrom
	 */
	public void setTransferredFrom(String transferredFrom) {
		this.transferredFrom = transferredFrom;
	}

	/**
	 * returns the quantity of cycles that are being transferred.
	 * @return quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * sets the quantity of transferred cycles.
	 * @param quantity
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * returns the vehicle number of the transport vehicle
	 * @return vehicleNo.
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

	/**
	 * Return the time of the arrival.
	 * @return arrivedOn
	 */
	public Timestamp getArrivedOn() {
		return arrivedOn;
	}

	/**
	 * sets the arriving time of the bicycle.
	 * @param arrivedOn
	 */
	public void setArrivedOn(Timestamp arrivedOn) {
		this.arrivedOn = arrivedOn;
	}

}
