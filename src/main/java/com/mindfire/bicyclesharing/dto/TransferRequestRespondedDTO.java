package com.mindfire.bicyclesharing.dto;

import java.sql.Timestamp;

import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.User;

public class TransferRequestRespondedDTO {

	private Long requestId;
	private PickUpPoint pickUpPoint;
	private User manager;
	private Integer quantity;
	private Timestamp requestedOn;
	private Boolean isApproved;
	private Boolean isResponded;

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

	/**
	 * @return the isResponded
	 */
	public Boolean getIsResponded() {
		return isResponded;
	}

	/**
	 * @param isResponded
	 *            the isResponded to set
	 */
	public void setIsResponded(Boolean isResponded) {
		this.isResponded = isResponded;
	}
}
