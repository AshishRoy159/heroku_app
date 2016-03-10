package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the pick_up_points database table.
 * 
 */
@Entity
@Table(name = "pick_up_points")
@NamedQuery(name = "PickUpPoint.findAll", query = "SELECT p FROM PickUpPoint p")
public class PickUpPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pick_up_point_id")
	private Integer pickUpPointId;

	@Column(name = "added_on")
	private Timestamp addedOn;

	@Column(name = "current_availability")
	private Integer currentAvailability;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_open")
	private Boolean isOpen;

	private String location;

	@Column(name = "max_capacity")
	private Integer maxCapacity;

	public PickUpPoint() {
	}

	/**
	 * @return the pickUpPointId
	 */
	public Integer getPickUpPointId() {
		return pickUpPointId;
	}

	/**
	 * @param pickUpPointId
	 *            the pickUpPointId to set
	 */
	public void setPickUpPointId(Integer pickUpPointId) {
		this.pickUpPointId = pickUpPointId;
	}

	/**
	 * @return the addedOn
	 */
	public Timestamp getAddedOn() {
		return addedOn;
	}

	/**
	 * @param addedOn
	 *            the addedOn to set
	 */
	public void setAddedOn(Timestamp addedOn) {
		this.addedOn = addedOn;
	}

	/**
	 * @return the currentAvailability
	 */
	public Integer getCurrentAvailability() {
		return currentAvailability;
	}

	/**
	 * @param currentAvailability
	 *            the currentAvailability to set
	 */
	public void setCurrentAvailability(Integer currentAvailability) {
		this.currentAvailability = currentAvailability;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the maxCapacity
	 */
	public Integer getMaxCapacity() {
		return maxCapacity;
	}

	/**
	 * @param maxCapacity
	 *            the maxCapacity to set
	 */
	public void setMaxCapacity(Integer maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
}
