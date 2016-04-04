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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
	private Long transferId;

	@Column(name = "arrived_on", insertable = false)
	private Timestamp arrivedOn;

	@Column(name = "dispatched_at", insertable = false)
	private Timestamp dispatchedAt;

	@Column(name = "quantity", updatable = false)
	private Integer quantity;

	@Column(name = "transferred_from", updatable = false)
	private PickUpPoint transferredFrom;

	@Column(name = "transferred_to", updatable = false)
	private PickUpPoint transferredTo;

	@Column(name = "vehicle_no", insertable = false)
	private String vehicleNo;

	@Column(name = "status", columnDefinition = "TRANSFERSTATUS")
	private String status;

	public Transfer() {
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
	public PickUpPoint getTransferredFrom() {
		return transferredFrom;
	}

	/**
	 * @param transferredFrom
	 *            the transferredFrom to set
	 */
	public void setTransferredFrom(PickUpPoint transferredFrom) {
		this.transferredFrom = transferredFrom;
	}

	/**
	 * @return the transferredTo
	 */
	public PickUpPoint getTransferredTo() {
		return transferredTo;
	}

	/**
	 * @param transferredTo
	 *            the transferredTo to set
	 */
	public void setTransferredTo(PickUpPoint transferredTo) {
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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
