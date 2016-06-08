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

/**
 * The persistent class for the booking_transaction database table.
 * 
 */
@Entity
@Table(name = "booking_transaction")
@NamedQuery(name = "BookingTransaction.findAll", query = "SELECT b FROM BookingTransaction b")
public class BookingTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_transaction_id")
	private Long bookingTransactionId;

	@ManyToOne
	@JoinColumn(name = "booking_id")
	private Booking booking;

	@ManyToOne
	@JoinColumn(name = "transaction_id")
	private WalletTransaction transaction;

	public BookingTransaction() {
	}

	/**
	 * @return the bookingTransactionId
	 */
	public Long getBookingTransactionId() {
		return bookingTransactionId;
	}

	/**
	 * @param bookingTransactionId
	 *            the bookingTransactionId to set
	 */
	public void setBookingTransactionId(Long bookingTransactionId) {
		this.bookingTransactionId = bookingTransactionId;
	}

	/**
	 * @return the booking
	 */
	public Booking getBooking() {
		return booking;
	}

	/**
	 * @param booking
	 *            the booking to set
	 */
	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	/**
	 * @return the transaction
	 */
	public WalletTransaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(WalletTransaction transaction) {
		this.transaction = transaction;
	}
}
