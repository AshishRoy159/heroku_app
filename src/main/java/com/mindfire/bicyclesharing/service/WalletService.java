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
import com.mindfire.bicyclesharing.component.WalletComponent;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.dto.WalletBalanceDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;

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
	private WalletComponent walletComponent;

	@Autowired
	private UserBookingComponent userBookingComponent;

	/**
	 * This method is used to add balance to the User's wallet
	 * 
	 * @param walletBalanceDTO
	 *            the data from the view
	 * @return Integer 0 or 1
	 */
	public int addBalance(WalletBalanceDTO walletBalanceDTO) {
		return walletComponent.mapWalletBalance(walletBalanceDTO);
	}

	/**
	 * This method is used to find wallet of a corresponding user.
	 * 
	 * @param user
	 *            User object
	 * @return {@link Wallet} object
	 */
	public Wallet getWallet(User user) {
		return walletComponent.findWalletByUser(user);
	}

	/**
	 * This method is used for saving the payment on user booking.
	 * 
	 * @param userBookingPaymentDTO
	 *            user booking payment data
	 * @param booking
	 *            booking details
	 * @param authentication
	 *            to get current logged in user details
	 * @return {@link WalletTransaction} object
	 */
	public WalletTransaction saveUserBookingPayment(UserBookingPaymentDTO userBookingPaymentDTO, Booking booking,
			Authentication authentication) {
		return userBookingComponent.mapUserBookingPayment(userBookingPaymentDTO, booking, authentication);
	}
}
