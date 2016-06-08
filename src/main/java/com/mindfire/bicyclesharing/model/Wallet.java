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
import java.util.List;

/**
 * The persistent class for the wallets database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "wallets")
@NamedQuery(name = "Wallet.findAll", query = "SELECT w FROM Wallet w")
public class Wallet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallet_id")
	private Integer walletId;

	private double balance;

	// bi-directional many-to-one association to WalletTransaction
	@OneToMany(mappedBy = "wallet")
	private List<WalletTransaction> walletTransactions;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Wallet() {
	}

	public Integer getWalletId() {
		return this.walletId;
	}

	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public List<WalletTransaction> getWalletTransactions() {
		return this.walletTransactions;
	}

	public void setWalletTransactions(List<WalletTransaction> walletTransactions) {
		this.walletTransactions = walletTransactions;
	}

	public WalletTransaction addWalletTransaction(WalletTransaction walletTransaction) {
		getWalletTransactions().add(walletTransaction);
		walletTransaction.setWallet(this);

		return walletTransaction;
	}

	public WalletTransaction removeWalletTransaction(WalletTransaction walletTransaction) {
		getWalletTransactions().remove(walletTransaction);
		walletTransaction.setWallet(null);

		return walletTransaction;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}