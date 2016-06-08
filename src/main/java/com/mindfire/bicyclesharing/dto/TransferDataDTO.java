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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.NumberFormat;

/**
 * TransferDataDTO class is used for taking data from the sendTransfer view
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class TransferDataDTO {

	@NotNull
	@Pattern(regexp = "^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$")
	private String vehicleNo;

	@NotNull
	@NumberFormat
	private Long transferId;

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

	/**
	 * @return the transferId
	 */
	public Long getTransferId() {
		return transferId;
	}

	/**
	 * @param transferId
	 *            the transferId to set
	 */
	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}
}
