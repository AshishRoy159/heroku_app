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

package com.mindfire.bicyclesharing.component;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.WalletTransactionRepository;

/**
 * WalletComponent class is used to get the data from the WalletService class
 * and set the data to the corresponding Entity classes
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class WalletTransactionComponent {

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	/**
	 * This method is used for getting all transaction based on their wallet.
	 * 
	 * @param wallet
	 *            Wallet object
	 * @return {@link List<WalletTransaction>}
	 */
	public List<WalletTransaction> mapWalletTransactionByWallet(Wallet wallet) {
		return walletTransactionRepository.findByWallet(wallet);
	}
}
