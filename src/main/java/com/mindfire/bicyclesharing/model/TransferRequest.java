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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The persistent class for the tranfer_requests database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "transfer_requests")
@NamedQuery(name = "TransferRequests.findAll", query = "SELECT tr FROM TransferRequest tr")
public class TransferRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(DataTablesOutput.View.class)
	@Column(name = "request_id")
	private Long requestId;

	@ManyToOne
	@JsonView(DataTablesOutput.View.class)
	@JoinColumn(name = "requesting_pickup_point")
	private PickUpPoint pickUpPoint;

	@ManyToOne
	@JsonView(DataTablesOutput.View.class)
	@JoinColumn(name = "requesting_manager")
	private User manager;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "quantity")
	private Integer quantity;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "approved_quantity", insertable = false, columnDefinition = "INTEGER DEFAULT 0")
	private Integer approvedQuantity;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "requested_on", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp requestedOn;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "is_approved", insertable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isApproved;

	/**
	 * @return the requestId
	 */
	public Long getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId
	 *            the requestId to set
	 */
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the pickUpPoint
	 */
	public PickUpPoint getPickUpPoint() {
		return pickUpPoint;
	}

	/**
	 * @param pickUpPoint
	 *            the pickUpPoint to set
	 */
	public void setPickUpPoint(PickUpPoint pickUpPoint) {
		this.pickUpPoint = pickUpPoint;
	}

	/**
	 * @return the manager
	 */
	public User getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	public void setManager(User manager) {
		this.manager = manager;
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
	 * @return the approvedQuantity
	 */
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	/**
	 * @param approvedQuantity
	 *            the approvedQuantity to set
	 */
	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	/**
	 * @return the requestedOn
	 */
	public Timestamp getRequestedOn() {
		return requestedOn;
	}

	/**
	 * @param requestedOn
	 *            the requestedOn to set
	 */
	public void setRequestedOn(Timestamp requestedOn) {
		this.requestedOn = requestedOn;
	}

	/**
	 * @return the isApproved
	 */
	public Boolean getIsApproved() {
		return isApproved;
	}

	/**
	 * @param isApproved
	 *            the isApproved to set
	 */
	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

}
