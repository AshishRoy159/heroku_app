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
 * The persistent class for the bookings database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "bookings")
@NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b")
public class Booking implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(DataTablesOutput.View.class)
	@Column(name = "booking_id")
	private Long bookingId;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "actual_in")
	private Timestamp actualIn;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "actual_out")
	private Timestamp actualOut;

	@JsonView(DataTablesOutput.View.class)
	@ManyToOne
	@JoinColumn(name = "bi_cycle_id")
	private BiCycle biCycleId;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "booking_time")
	private Timestamp bookingTime;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "expected_in")
	private Timestamp expectedIn;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "expected_out")
	private Timestamp expectedOut;

	@JsonView(DataTablesOutput.View.class)
	private double fare;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "is_open")
	private Boolean isOpen;

	@JsonView(DataTablesOutput.View.class)
	@Column(name = "is_used", insertable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean isUsed;

	@ManyToOne
	@JsonView(DataTablesOutput.View.class)
	@JoinColumn(name = "picked_up_from")
	private PickUpPoint pickedUpFrom;

	@ManyToOne
	@JsonView(DataTablesOutput.View.class)
	@JoinColumn(name = "returnet_at")
	private PickUpPoint returnedAt;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JsonView(DataTablesOutput.View.class)
	@JoinColumn(name = "user_id")
	private User user;

	public Booking() {
	}

	/**
	 * @return the bookingId
	 */
	public Long getBookingId() {
		return bookingId;
	}

	/**
	 * @param bookingId
	 *            the bookingId to set
	 */
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	/**
	 * @return the actualIn
	 */
	public Timestamp getActualIn() {
		return actualIn;
	}

	/**
	 * @param actualIn
	 *            the actualIn to set
	 */
	public void setActualIn(Timestamp actualIn) {
		this.actualIn = actualIn;
	}

	/**
	 * @return the actualOut
	 */
	public Timestamp getActualOut() {
		return actualOut;
	}

	/**
	 * @param actualOut
	 *            the actualOut to set
	 */
	public void setActualOut(Timestamp actualOut) {
		this.actualOut = actualOut;
	}

	/**
	 * @return the biCycleId
	 */
	public BiCycle getBiCycleId() {
		return biCycleId;
	}

	/**
	 * @param biCycleId
	 *            the biCycleId to set
	 */
	public void setBiCycleId(BiCycle biCycleId) {
		this.biCycleId = biCycleId;
	}

	/**
	 * @return the bookingTime
	 */
	public Timestamp getBookingTime() {
		return bookingTime;
	}

	/**
	 * @param bookingTime
	 *            the bookingTime to set
	 */
	public void setBookingTime(Timestamp bookingTime) {
		this.bookingTime = bookingTime;
	}

	/**
	 * @return the expectedIn
	 */
	public Timestamp getExpectedIn() {
		return expectedIn;
	}

	/**
	 * @param expectedIn
	 *            the expectedIn to set
	 */
	public void setExpectedIn(Timestamp expectedIn) {
		this.expectedIn = expectedIn;
	}

	/**
	 * @return the expectedOut
	 */
	public Timestamp getExpectedOut() {
		return expectedOut;
	}

	/**
	 * @param expectedOut
	 *            the expectedOut to set
	 */
	public void setExpectedOut(Timestamp expectedOut) {
		this.expectedOut = expectedOut;
	}

	/**
	 * @return the fare
	 */
	public double getFare() {
		return fare;
	}

	/**
	 * @param fare
	 *            the fare to set
	 */
	public void setFare(double fare) {
		this.fare = fare;
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
	 * @return the pickedUpFrom
	 */
	public PickUpPoint getPickedUpFrom() {
		return pickedUpFrom;
	}

	/**
	 * @param pickedUpFrom
	 *            the pickedUpFrom to set
	 */
	public void setPickedUpFrom(PickUpPoint pickedUpFrom) {
		this.pickedUpFrom = pickedUpFrom;
	}

	/**
	 * @return the returnedAt
	 */
	public PickUpPoint getReturnedAt() {
		return returnedAt;
	}

	/**
	 * @param returnedAt
	 *            the returnedAt to set
	 */
	public void setReturnedAt(PickUpPoint returnedAt) {
		this.returnedAt = returnedAt;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the isUsed
	 */
	public Boolean getIsUsed() {
		return isUsed;
	}

	/**
	 * @param isUsed
	 *            the isUsed to set
	 */
	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}
}
