package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the bi_cycles database table.
 * 
 */
@Entity
@Table(name = "bi_cycles")
@NamedQuery(name = "BiCycle.findAll", query = "SELECT b FROM BiCycle b")
public class BiCycle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bi_cycle_id")
	private Integer biCycleId;

	@Column(name = "added_on")
	private Timestamp addedOn;

	@Column(name = "chasis_no")
	private String chasisNo;

	@Column(name = "current_location")
	private String currentLocation;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_available")
	private Boolean isAvailable;

	public BiCycle() {
	}

	/**
	 * @return the biCycleId
	 */
	public Integer getBiCycleId() {
		return biCycleId;
	}

	/**
	 * @param biCycleId
	 *            the biCycleId to set
	 */
	public void setBiCycleId(Integer biCycleId) {
		this.biCycleId = biCycleId;
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
	 * @return the chasisNo
	 */
	public String getChasisNo() {
		return chasisNo;
	}

	/**
	 * @param chasisNo
	 *            the chasisNo to set
	 */
	public void setChasisNo(String chasisNo) {
		this.chasisNo = chasisNo;
	}

	/**
	 * @return the currentLocation
	 */
	public String getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * @param currentLocation
	 *            the currentLocation to set
	 */
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
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
	 * @return the isAvailable
	 */
	public Boolean getIsAvailable() {
		return isAvailable;
	}

	/**
	 * @param isAvailable
	 *            the isAvailable to set
	 */
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}
