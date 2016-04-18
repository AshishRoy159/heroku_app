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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.WalletBalanceDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;
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

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	/**
	 * This method is used for getting all transaction based on their wallet.
	 * 
	 * @param wallet
	 *            Wallet object
	 * @return {@link WalletTransaction} List
	 */
	public List<WalletTransaction> mapWalletTransactionByWallet(Wallet wallet) {
		return walletTransactionRepository.findByWallet(wallet);
	}

	/**
	 * This method is used to create new wallet transaction record
	 * 
	 * @param walletBalanceDTO
	 *            the incoming wallet transaction data
	 * @return {@link WalletTransaction} object
	 */
	public WalletTransaction mapWalletTransactionDetails(WalletBalanceDTO walletBalanceDTO) {
		WalletTransaction walletTransaction = new WalletTransaction();

		walletTransaction.setAmount(walletBalanceDTO.getBalance());
		walletTransaction
				.setWallet(walletRepository.findByUser(userRepository.findByUserId(walletBalanceDTO.getUserId())));
		walletTransaction.setMode("cash");
		walletTransaction.setType("DEPOSIT");

		try {
			logger.info("Created new wallet transaction.");
			return walletTransactionRepository.save(walletTransaction);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}
}
