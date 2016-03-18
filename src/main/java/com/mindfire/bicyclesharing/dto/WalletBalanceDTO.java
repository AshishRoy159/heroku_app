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

package com.mindfire.bicyclesharing.dto;

/**
 * WalletBalanceDTO class is used for taking data from the Add Wallet Balance
 * view.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
public class WalletBalanceDTO {

	private String walletId;
	private String userId;
	private Double balance;

	/**
	 * returns the wallet Id
	 * @return walletId
	 */
	public String getWalletId() {
		return walletId;
	}

	/**
	 * sets the wallet Id
	 * @param walletId
	 */
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	/**
	 * returns the userId
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * sets the UserId.
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * returns the balance.
	 * @return balance.
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * sets the balance
	 * @param balance
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
