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
