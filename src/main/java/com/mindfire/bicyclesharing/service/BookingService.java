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

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.BookingComponent;
import com.mindfire.bicyclesharing.component.UserBookingComponent;
import com.mindfire.bicyclesharing.dto.BookingPaymentDTO;
import com.mindfire.bicyclesharing.dto.IssueCycleForOnlineDTO;
import com.mindfire.bicyclesharing.dto.PaymentAtPickUpPointDTO;
import com.mindfire.bicyclesharing.dto.ReceiveCycleDTO;
import com.mindfire.bicyclesharing.dto.UserBookingDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.WalletTransaction;

/**
 * BookingService class contains methods for Booking related operations
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Service
public class BookingService {

	@Autowired
	private BookingComponent bookingComponent;

	@Autowired
	private UserBookingComponent userBookingComponent;

	/**
	 * This method is used to add a new booking entry to the database.
	 * 
	 * @param authentication
	 *            this object is used for retrieving the current user details.
	 * @param bookingPaymentDTO
	 *            BookingPaymentDTO object for issue BiCycle data
	 * @param session
	 *            for session data
	 * @return Booking object.
	 */
	public Booking addNewBooking(Authentication authentication, BookingPaymentDTO bookingPaymentDTO,
			HttpSession session) {
		return bookingComponent.mapNewBooking(authentication, bookingPaymentDTO, session);
	}

	/**
	 * This method is used to execute receive operation of a bicycle.
	 * 
	 * @param id
	 *            bicycleId
	 * @param fare
	 *            total fare amount
	 * @param authentication
	 *            authentication object for current manager.
	 * @return {@link Booking} object
	 */
	public Booking receiveBicycle(Long id, double fare, Authentication authentication) {
		return bookingComponent.mapReceiveBicycle(id, fare, authentication);
	}

	/**
	 * This method is used for saving the user booking details.
	 * 
	 * @param userBookingDTO
	 *            user booking data
	 * @param authentication
	 *            to get current logged in user data
	 * @return {@link Booking} object
	 */
	public Booking saveUserBookingDetails(UserBookingDTO userBookingDTO, Authentication authentication) {
		return userBookingComponent.setUserBookingDetails(userBookingDTO, authentication);
	}

	/**
	 * This method is used for sending the control and data to the corresponding
	 * component class for updating the issue bicycle details.
	 * 
	 * @param issueCycleForOnlineDTO
	 *            this object holds the issue bicycle details data.
	 * @param fare
	 *            booking fare
	 * @return {@link Booking} object
	 */
	public Booking updateIssueBicycleDetails(IssueCycleForOnlineDTO issueCycleForOnlineDTO, Double fare) {
		return userBookingComponent.mapIssueBicycleDetails(issueCycleForOnlineDTO.getBookingId(),
				issueCycleForOnlineDTO.getBicycleId(), fare);
	}

	/**
	 * This method is used for sending the control and data to the corresponding
	 * component class for updating the issue bicycle details along with
	 * payment.
	 * 
	 * @param paymentAtPickUpPointDTO
	 *            this object holds the issue bicycle payment related data.
	 * @return {@link Booking} object
	 */
	public Booking updateIssueBicycleDetailsWithPayment(PaymentAtPickUpPointDTO paymentAtPickUpPointDTO) {
		return userBookingComponent.mapIssueBicycleDetails(paymentAtPickUpPointDTO.getBookingId(),
				paymentAtPickUpPointDTO.getBicycleId(), paymentAtPickUpPointDTO.getFare());
	}

	/**
	 * This method is used for sending the control and data to the corresponding
	 * component class for creating the payment transaction for the user booking
	 * 
	 * @param paymentAtPickUpPointDTO
	 *            payment related data
	 * @return {@link WalletTransaction} object
	 */
	public WalletTransaction createUserPaymentTransaction(PaymentAtPickUpPointDTO paymentAtPickUpPointDTO) {
		return bookingComponent.mapUserPaymentTransaction(paymentAtPickUpPointDTO);
	}

	/**
	 * This method is used for getting the booking details by booking id.
	 * 
	 * @param bookingId
	 *            this is booking id
	 * @return {@link Booking} object
	 */
	public Booking getBookingById(Long bookingId) {
		return userBookingComponent.getBooking(bookingId);
	}

	/**
	 * This method is used for getting all booking details based or particular
	 * user and their booking status.
	 * 
	 * @param user
	 *            User object
	 * @param isOpen
	 *            this is Boolean value
	 * @return {@link Booking} List
	 */
	public List<Booking> getAllBooking(User user, Boolean isOpen) {
		return bookingComponent.getAllBookingByUser(user, isOpen);
	}

	/**
	 * This method is used for getting all booking details based on booking
	 * status.
	 * 
	 * @param isUsed
	 *            this is Boolean value
	 * @return {@link Booking} List
	 */
	public List<Booking> getAllBookingDetails(Boolean isUsed) {
		return bookingComponent.getAllBooking(isUsed);
	}

	/**
	 * This method is used to close unused booking.
	 * 
	 * @param receiveCycleDTO
	 * @return {@link Booking}
	 */
	public Booking closeBooking(ReceiveCycleDTO receiveCycleDTO) {
		return bookingComponent.mapCloseBooking(receiveCycleDTO);
	}
}
