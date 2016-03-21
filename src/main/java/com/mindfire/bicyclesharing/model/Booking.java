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
	@Column(name = "booking_id")
	private Integer bookingId;

	@Column(name = "actual_in")
	private Timestamp actualIn;

	@Column(name = "actual_out")
	private Timestamp actualOut;

	@ManyToOne
	@JoinColumn(name = "bi_cycle_id")
	private BiCycle biCycleId;

	@Column(name = "booking_time")
	private Timestamp bookingTime;

	@Column(name = "expected_in")
	private Timestamp expectedIn;

	@Column(name = "expected_out")
	private Timestamp expectedOut;

	private double fare;

	@Column(name = "is_open")
	private Boolean isOpen;

	@Column(name = "picked_up_from")
	private String pickedUpFrom;

	@Column(name = "returned_at")
	private String returnedAt;

	@Column(name = "transaction_id")
	private String transactionId;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Booking() {
	}

	/**
	 * @return the bookingId
	 */
	public Integer getBookingId() {
		return bookingId;
	}

	/**
	 * @param bookingId
	 *            the bookingId to set
	 */
	public void setBookingId(Integer bookingId) {
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
	public String getPickedUpFrom() {
		return pickedUpFrom;
	}

	/**
	 * @param pickedUpFrom
	 *            the pickedUpFrom to set
	 */
	public void setPickedUpFrom(String pickedUpFrom) {
		this.pickedUpFrom = pickedUpFrom;
	}

	/**
	 * @return the returnedAt
	 */
	public String getReturnedAt() {
		return returnedAt;
	}

	/**
	 * @param returnedAt
	 *            the returnedAt to set
	 */
	public void setReturnedAt(String returnedAt) {
		this.returnedAt = returnedAt;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
}
