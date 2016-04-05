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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the bi_cycle_transfer database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "bi_cycle_transfer")
@NamedQuery(name = "BiCycleTransfer.findAll", query = "SELECT b FROM BiCycleTransfer b")
public class BiCycleTransfer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bicycle_transfer_id")
	private Long biCycleTransferId;

	@ManyToOne
	@JoinColumn(name = "bi_cycle_id")
	private BiCycle biCycle;

	@ManyToOne
	@JoinColumn(name = "transfer_id")
	private Transfer transfer;

	public BiCycleTransfer() {
	}

	/**
	 * @return the biCycleTransferId
	 */
	public Long getBiCycleTransferId() {
		return biCycleTransferId;
	}

	/**
	 * @param biCycleTransferId
	 *            the biCycleTransferId to set
	 */
	public void setBiCycleTransferId(Long biCycleTransferId) {
		this.biCycleTransferId = biCycleTransferId;
	}

	/**
	 * @return the biCycle
	 */
	public BiCycle getBiCycle() {
		return biCycle;
	}

	/**
	 * @param biCycle
	 *            the biCycle to set
	 */
	public void setBiCycle(BiCycle biCycle) {
		this.biCycle = biCycle;
	}

	/**
	 * @return the transfer
	 */
	public Transfer getTransfer() {
		return transfer;
	}

	/**
	 * @param transfer
	 *            the transfer to set
	 */
	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

}
