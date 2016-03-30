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

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.BookingPaymentDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.UserRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;
import com.mindfire.bicyclesharing.repository.WalletTransactionRepository;

/**
 * BookingComponent class is used to get the data from the IssueCycleDTO class
 * and set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class BookingComponent {

	@Autowired
	private BiCycleRepository bicycleRepository;

	@Autowired
	private PickUpPointManagerRepository managerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	/**
	 * This method is used for receiving the data from IssueCycleDTO object and
	 * set the data to the corresponding entity class
	 * 
	 * @param authentication
	 * @param issueCycleDTO
	 *            the data from the view
	 * @return Booking object
	 */
	public Booking mapNewBooking(Authentication authentication, BookingPaymentDTO bookingPaymentDTO,
			HttpSession session) {
		User user = userRepository.findByUserId(bookingPaymentDTO.getUserId());
		Wallet userWallet = walletRepository.findByUser(user);
		if (bookingPaymentDTO.getMode().equals("wallet")) {
			if (userWallet.getBalance() < bookingPaymentDTO.getAmount()) {
				return null;
			} else {
				userWallet.setBalance(userWallet.getBalance() - bookingPaymentDTO.getAmount());
				walletRepository.save(userWallet);
				createWalletTransaction(bookingPaymentDTO, userWallet);

			}
		} else {
			createWalletTransaction(bookingPaymentDTO, userWallet);
		}

		return createNewBooking(authentication, bookingPaymentDTO, session);
	}

	/**
	 * This method is used to create the wallet transaction details.
	 * 
	 * @param bookingPaymentDTO
	 *            BookingPaymentDTO object for bookingPayment related Data.
	 * @param userWallet
	 *            this object contains UserWallet information.
	 */
	private void createWalletTransaction(BookingPaymentDTO bookingPaymentDTO, Wallet userWallet) {
		WalletTransaction walletTransaction = new WalletTransaction();
		walletTransaction.setAmount(bookingPaymentDTO.getAmount());
		walletTransaction.setMode(bookingPaymentDTO.getMode());
		walletTransaction.setType("BOOKING");
		walletTransaction.setWallet(userWallet);
		walletTransactionRepository.save(walletTransaction);
	}

	/**
	 * This method is used to create new Booking for user.
	 * 
	 * @param authentication
	 *            this object is used for retrieving the current user details.
	 * @param bookingPaymentDTO
	 *            this object is used for holding the booking related data..
	 * @param session
	 *            HttpSession object is used for holding the required value into
	 *            the session variable.
	 * @return Booking object
	 */
	private Booking createNewBooking(Authentication authentication, BookingPaymentDTO bookingPaymentDTO,
			HttpSession session) {
		Booking newBooking = new Booking();
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		newBooking.setActualOut(new Timestamp(System.currentTimeMillis()));
		newBooking.setBiCycleId(bicycleRepository.findByBiCycleId(bookingPaymentDTO.getBicycleId()));
		newBooking.setBookingTime(new Timestamp(System.currentTimeMillis()));
		newBooking.setExpectedIn((Timestamp) session.getAttribute("expectedIn"));
		newBooking.setExpectedOut(new Timestamp(System.currentTimeMillis()));
		newBooking.setIsOpen(true);
		newBooking.setPickedUpFrom(
				managerRepository.findByUser(userRepository.findByUserId(currentUser.getUserId())).getPickUpPoint());
		newBooking.setUser(userRepository.findByUserId(bookingPaymentDTO.getUserId()));
		newBooking.setFare(bookingPaymentDTO.getAmount());
		Booking bookSuccess = bookingRepository.save(newBooking);

		return bookSuccess;
	}
}
