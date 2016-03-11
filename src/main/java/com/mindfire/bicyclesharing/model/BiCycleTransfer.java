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
