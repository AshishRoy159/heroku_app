package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the wallet_transactions database table.
 * 
 */
@Entity
@Table(name = "wallet_transactions")
@NamedQuery(name = "WalletTransaction.findAll", query = "SELECT w FROM WalletTransaction w")
public class WalletTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Integer transactionId;

	private double amount;

	private String mode;

	@Column(name = "transcation_time")
	private Timestamp transcationTime;

	private String type;

	@Column(name = "wallet_id")
	private String walletId;

	public WalletTransaction() {
	}

	/**
	 * @return the transactionId
	 */
	public Integer getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the transcationTime
	 */
	public Timestamp getTranscationTime() {
		return transcationTime;
	}

	/**
	 * @param transcationTime
	 *            the transcationTime to set
	 */
	public void setTranscationTime(Timestamp transcationTime) {
		this.transcationTime = transcationTime;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the walletId
	 */
	public String getWalletId() {
		return walletId;
	}

	/**
	 * @param walletId
	 *            the walletId to set
	 */
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
}
