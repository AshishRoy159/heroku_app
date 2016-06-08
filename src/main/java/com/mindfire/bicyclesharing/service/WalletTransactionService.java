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

package com.mindfire.bicyclesharing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.WalletTransactionComponent;
import com.mindfire.bicyclesharing.dto.WalletBalanceDTO;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;

/**
 * This class contain methods for wallet transaction related operations.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 *
 */
@Service
public class WalletTransactionService {

	@Autowired
	private WalletTransactionComponent walletTransactionComponent;

	/**
	 * This method is used for accepting the control from the wallet transaction
	 * controller and sends the control to the walletTransactionComponent along
	 * with wallet for getting the wallet transaction details.
	 * 
	 * @param wallet
	 *            Wallet object
	 * @return {@link WalletTransaction} List
	 */
	public List<WalletTransaction> getAllTransactionByWallet(Wallet wallet) {
		return walletTransactionComponent.mapWalletTransactionByWallet(wallet);
	}

	/**
	 * This method is used to create new wallet transaction object
	 * 
	 * @param walletBalanceDTO
	 *            the incoming wallet transaction data
	 * @return {@link WalletTransaction} object
	 */
	public WalletTransaction createWalletTransaction(WalletBalanceDTO walletBalanceDTO) {
		return walletTransactionComponent.mapWalletTransactionDetails(walletBalanceDTO);
	}
}
