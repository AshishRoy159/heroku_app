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
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.CurrentUser;
import com.mindfire.bicyclesharing.dto.BookingPaymentDTO;
import com.mindfire.bicyclesharing.dto.PaymentAtPickUpPointDTO;
import com.mindfire.bicyclesharing.dto.ReceiveBicyclePaymentDTO;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointManagerRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;
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

	@Autowired
	private PickUpPointManagerRepository pickUpPointManagerRepository;

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

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

		String transactionType = "BOOKING";
		if (bookingPaymentDTO.getMode().equals("wallet")) {
			if (userWallet.getBalance() < bookingPaymentDTO.getAmount()) {
				return null;
			} else {
				userWallet.setBalance(userWallet.getBalance() - bookingPaymentDTO.getAmount());
				walletRepository.save(userWallet);
				createWalletTransaction(bookingPaymentDTO.getAmount(), bookingPaymentDTO.getMode(), transactionType,
						userWallet);

			}
		} else {
			createWalletTransaction(bookingPaymentDTO.getAmount(), bookingPaymentDTO.getMode(), transactionType,
					userWallet);
		}

		return createNewBooking(authentication, bookingPaymentDTO, session);
	}

	/**
	 * This method is used to create a wallet transaction details.
	 * 
	 * @param amount
	 *            fare
	 * @param mode
	 *            payment mode
	 * @param type
	 *            payment type
	 * @param userWallet
	 *            this object contains UserWallet information.
	 * @return WalletTransaction object
	 */
	private WalletTransaction createWalletTransaction(Double amount, String mode, String type, Wallet userWallet) {
		WalletTransaction walletTransaction = new WalletTransaction();
		walletTransaction.setAmount(amount);
		walletTransaction.setMode(mode);
		walletTransaction.setType(type);
		walletTransaction.setWallet(userWallet);
		walletTransactionRepository.save(walletTransaction);

		return walletTransaction;
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

	/**
	 * This method is used for mapping receive bicycle data and update into the
	 * booking details field along with the booking status.
	 * 
	 * @param id
	 *            this id is booking id
	 * @param fare
	 * @param authentication
	 * @return Booking object
	 */
	public Booking mapReceiveBicycle(Long id, double fare, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();

		Booking booking = bookingRepository.findByBookingId(id);
		booking.setActualIn(new Timestamp(System.currentTimeMillis()));
		booking.setReturnedAt(pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint());
		booking.setIsOpen(false);
		booking.setFare(booking.getFare() + fare);

		bookingRepository.save(booking);

		BiCycle biCycle = bicycleRepository.findByBiCycleId(booking.getBiCycleId().getBiCycleId());
		biCycle.setCurrentLocation(booking.getReturnedAt());
		biCycle.setIsAvailable(true);
		bicycleRepository.save(biCycle);

		PickUpPoint pickUpPoint = pickUpPointManagerRepository.findByUser(currentUser.getUser()).getPickUpPoint();
		pickUpPoint.setCurrentAvailability(
				bicycleRepository.findByCurrentLocationAndIsAvailable(pickUpPoint, true).size());
		pickUpPointRepository.save(pickUpPoint);

		return booking;
	}

	/**
	 * This method is used for mapping the receive bicycle payment data at the
	 * time of bicycle receive.
	 * 
	 * @param receiveBicyclePaymentDTO
	 *            receive bicycle payment data
	 * @param userWallet
	 *            UserWallet object related to corresponding user.
	 * @return WalletTransaction object
	 */
	public WalletTransaction mapReceiveBicyclePayment(ReceiveBicyclePaymentDTO receiveBicyclePaymentDTO,
			Wallet userWallet) {
		String transactionType = "RECEIVEBICYCLE";

		if (receiveBicyclePaymentDTO.getMode().equals("cash")) {
			WalletTransaction walletTransaction = createWalletTransaction(receiveBicyclePaymentDTO.getFare(),
					receiveBicyclePaymentDTO.getMode(), transactionType, userWallet);
			return walletTransaction;
		} else if (userWallet.getBalance() < receiveBicyclePaymentDTO.getFare()) {
			return null;
		} else {
			userWallet.setBalance(userWallet.getBalance() - receiveBicyclePaymentDTO.getFare());
			walletRepository.save(userWallet);
			WalletTransaction walletTransaction = createWalletTransaction(receiveBicyclePaymentDTO.getFare(),
					receiveBicyclePaymentDTO.getMode(), transactionType, userWallet);
			return walletTransaction;

		}
	}

	/**
	 * This method is used for creating a wallet transaction for user booking.
	 * 
	 * @param userBookingPaymentDTO
	 *            User Booking Payment data
	 * @param userWallet
	 *            UserWallet object related to corresponding user.
	 * @param type
	 *            payment Type
	 * @return WalletTransaction object
	 */
	public WalletTransaction userBookingWalletTransaction(UserBookingPaymentDTO userBookingPaymentDTO,
			Wallet userWallet, String type) {
		return createWalletTransaction(userBookingPaymentDTO.getFare(), userBookingPaymentDTO.getMode(), type,
				userWallet);
	}

	/**
	 *  This method is used for creating a wallet transaction for user booking.
	 * 
	 * @param paymentAtPickUpPointDTO
	 *            User Booking Payment data
	 * 
	 * @return WalletTransaction object
	 */
	public WalletTransaction mapUserPaymentTransaction(PaymentAtPickUpPointDTO paymentAtPickUpPointDTO) {
		String paymentType = "ONLINE BOOKING";
		String mode = "cash";
		Wallet userWallet = walletRepository
				.findByUser(bookingRepository.findByBookingId(paymentAtPickUpPointDTO.getBookingId()).getUser());
		return createWalletTransaction(paymentAtPickUpPointDTO.getFare(), mode, paymentType, userWallet);
	}
	
	public List<Booking> getAllBookingByUser(User user){
		return bookingRepository.findAllByUser(user);
	}
}
