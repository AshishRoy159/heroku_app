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
	@Column(name = "booking_id")
	private Integer bookingId;

	@Column(name = "transaction_id")
	private String transactionId;

	public BookingTransaction() {
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
}
