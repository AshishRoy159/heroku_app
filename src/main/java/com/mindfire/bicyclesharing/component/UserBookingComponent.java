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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.UserBookingDTO;
import com.mindfire.bicyclesharing.dto.UserBookingPaymentDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Wallet;
import com.mindfire.bicyclesharing.model.WalletTransaction;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;
import com.mindfire.bicyclesharing.repository.WalletRepository;
import com.mindfire.bicyclesharing.security.CurrentUser;

/**
 * UserBookingComponent class is used to get the data from the particular DTO
 * class and set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class UserBookingComponent {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private BookingComponent bookingComponent;

	@Autowired
	private BiCycleRepository biCycleRepository;

	/**
	 * This method is used to set user booking data to corresponding entity
	 * classes.
	 * 
	 * @param userBookingDTO
	 *            this parameter holds user booking related data.
	 * @param authentication
	 *            this parameter is used to identify the current user.
	 * @return {@link Booking}
	 */
	public Booking setUserBookingDetails(UserBookingDTO userBookingDTO, Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Booking userBooking = new Booking();
		userBooking.setBookingTime(new Timestamp(System.currentTimeMillis()));
		userBooking
				.setExpectedOut(Timestamp.valueOf(userBookingDTO.getBookingTime().replace("/", "-").concat(":00.000")));
		userBooking
				.setExpectedIn(Timestamp.valueOf(userBookingDTO.getReturnTime().replace("/", "-").concat(":00.000")));
		userBooking.setUser(currentUser.getUser());
		userBooking.setIsOpen(true);
		userBooking.setPickedUpFrom(pickUpPointRepository.findByPickUpPointId(userBookingDTO.getPickUpPoint()));

		try {
			return bookingRepository.save(userBooking);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used for making the user payment.
	 * 
	 * @param userBookingPaymentDTO
	 *            this parameter holds the user payment data.
	 * @param authentication
	 *            this parameter is used to identify the current user.
	 * @return {@link WalletTransaction}
	 */
	public WalletTransaction mapUserBookingPayment(UserBookingPaymentDTO userBookingPaymentDTO,
			Authentication authentication) {
		CurrentUser currentUser = (CurrentUser) authentication.getPrincipal();
		Wallet userWallet = walletRepository.findByUser(currentUser.getUser());
		String paymentType = "ONLINE BOOKING";
		if (userBookingPaymentDTO.getMode().equals("wallet")) {
			if (userWallet.getBalance() < userBookingPaymentDTO.getFare()) {
				return null;
			} else {
				userWallet.setBalance(userWallet.getBalance() - userBookingPaymentDTO.getFare());
				try {
					walletRepository.save(userWallet);
				} catch (DataIntegrityViolationException dataIntegrityViolationException) {
					throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
				}
				Booking userBooking = bookingRepository.findByBookingId(userBookingPaymentDTO.getBookingId());
				userBooking.setFare(userBookingPaymentDTO.getFare());
				try {
					bookingRepository.save(userBooking);
				} catch (Exception e) {
					throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
				}
				return bookingComponent.userBookingWalletTransaction(userBookingPaymentDTO, userWallet, paymentType);
			}
		} else {
			return bookingComponent.userBookingWalletTransaction(userBookingPaymentDTO, userWallet, paymentType);
		}
	}

	/**
	 * This method is used to set the user booking details to the corresponding
	 * entity classes.
	 * 
	 * @param bookingId
	 *            this parameter holds the booking Id.
	 * @param bicycleId
	 *            this parameter holds the bicycle Id.
	 * @param fare
	 *            this parameter holds the booking fare.
	 * @return {@link Booking}
	 */
	public Booking mapIssueBicycleDetails(Long bookingId, Long bicycleId, Double fare) {
		Booking userBooking = bookingRepository.findByBookingId(bookingId);
		BiCycle bicycle = biCycleRepository.findByBiCycleId(bicycleId);
		bicycle.setIsAvailable(false);
		userBooking.setActualOut(new Timestamp(System.currentTimeMillis()));
		userBooking.setBiCycleId(biCycleRepository.save(bicycle));
		userBooking.setIsOpen(true);
		userBooking.setFare(fare);
		PickUpPoint pickUpPoint = pickUpPointRepository.findByPickUpPointId(bicycle.getCurrentLocation().getPickUpPointId());
		pickUpPoint.setCurrentAvailability(
				biCycleRepository.findByCurrentLocationAndIsAvailable(pickUpPoint, true).size());
		try {
			pickUpPointRepository.save(pickUpPoint);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		
		try {
			return bookingRepository.save(userBooking);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * This method is used for getting the booking.
	 * 
	 * @param bookingId
	 *            this is bookingId
	 * @return {@link Booking}
	 */
	public Booking getBooking(Long bookingId) {
		return bookingRepository.findByBookingId(bookingId);
	}
}
