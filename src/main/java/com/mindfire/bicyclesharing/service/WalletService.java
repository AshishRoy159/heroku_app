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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.UserBookingComponent;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.WalletRepository;
import com.mindfire.bicyclesharing.repository.WalletTransactionRepository;

/**
 * This class contain methods for wallet related operations.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 *
 */
@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	@Autowired
	private UserBookingComponent userBookingComponent;

	/**
	 * This method is used to add balance to the User's wallet
	 * 
	 * @param user
	 *            User object
	 * @param balance
	 *            amount to be added to wallet
	 * @return Integer 0 or 1
	 */
	public int addBalance(User user, Double balance) {
		return walletRepository.updateBalance(balance, user);
	}

	/**
	 * This method is used to find wallet of a corresponding user.
	 * 
	 * @param user
	 *            User object
	 * @return Wallet object
	 */
	public Wallet getWallet(User user) {
		return walletRepository.findByUser(user);
	}

	/**
	 * This method is used to save wallet transaction related data to the
	 * database.
	 * 
	 * @param walletTransaction
	 *            WalletTransaction details to be saved in the database
	 * @return WalletTransaction object
	 */
	public WalletTransaction saveWalletTransactionDetail(WalletTransaction walletTransaction) {
		return walletTransactionRepository.save(walletTransaction);

	}

	/**
	 * This method is used for saving the payment on user booking.
	 * 
	 * @param userBookingPaymentDTO
	 *            user booking payment data
	 * @param authentication
	 * @return {@link WalletTransaction}
	 */
	public WalletTransaction saveUserBookingPayment(UserBookingPaymentDTO userBookingPaymentDTO,
			Authentication authentication) {
		return userBookingComponent.mapUserBookingPayment(userBookingPaymentDTO, authentication);
	}
}
