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

package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the transfers database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "transfers")
@NamedQuery(name = "Transfer.findAll", query = "SELECT t FROM Transfer t")
public class Transfer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transfer_id")
	private Integer transferId;

	@Column(name = "arrived_on")
	private Timestamp arrivedOn;

	@Column(name = "dispatched_at")
	private Timestamp dispatchedAt;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "transferred_from")
	private String transferredFrom;

	@Column(name = "transferred_to")
	private String transferredTo;

	@Column(name = "vehicle_no")
	private String vehicleNo;

	@Column(name = "is_open")
	private Boolean isOpen;

	public Transfer() {
	}

	/**
	 * @return the transferId
	 */
	public Integer getTransferId() {
		return transferId;
	}

	/**
	 * @param transferId
	 *            the transferId to set
	 */
	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}

	/**
	 * @return the arrivedOn
	 */
	public Timestamp getArrivedOn() {
		return arrivedOn;
	}

	/**
	 * @param arrivedOn
	 *            the arrivedOn to set
	 */
	public void setArrivedOn(Timestamp arrivedOn) {
		this.arrivedOn = arrivedOn;
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
	 * @return the transferredFrom
	 */
	public String getTransferredFrom() {
		return transferredFrom;
	}

	/**
	 * @param transferredFrom
	 *            the transferredFrom to set
	 */
	public void setTransferredFrom(String transferredFrom) {
		this.transferredFrom = transferredFrom;
	}

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
	 * @return the isOpen
	 */
	public Boolean getIsOpen() {
		return isOpen;
	}

	/**
	 * @param isOpen
	 *            the isOpen to set
	 */
	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

}
