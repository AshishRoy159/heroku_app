package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the bi_cycle_transfer database table.
 * 
 */
@Entity
@Table(name = "bi_cycle_transfer")
@NamedQuery(name = "BiCycleTransfer.findAll", query = "SELECT b FROM BiCycleTransfer b")
public class BiCycleTransfer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bi_cycle_id")
	private Integer biCycleId;

	@Column(name = "transfer_id")
	private String transferId;

	public BiCycleTransfer() {
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
	 * @return the transferId
	 */
	public String getTransferId() {
		return transferId;
	}

	/**
	 * @param transferId
	 *            the transferId to set
	 */
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
}
