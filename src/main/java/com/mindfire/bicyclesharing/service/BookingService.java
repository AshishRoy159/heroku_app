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
	 * @param issueCycleDTO
	 *            IssueCycleDTO object for issue BiCycle data
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
	 * @param authentication
	 *            authentication object for current manager.
	 * @return {@link Booking}
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
	 * @return {@link Booking}
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
	 * @return {@link Booking}
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
	 * @return {@link Booking}
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
	 * @return {@link WalletTransaction}
	 */
	public WalletTransaction createUserPaymentTransaction(PaymentAtPickUpPointDTO paymentAtPickUpPointDTO) {
		return bookingComponent.mapUserPaymentTransaction(paymentAtPickUpPointDTO);
	}

	/**
	 * This method is used for getting the booking details by booking id.
	 * 
	 * @param bookingId
	 *            this is booking id
	 * @return {@link Booking}
	 */
	public Booking getBookingById(Long bookingId) {
		return userBookingComponent.getBooking(bookingId);
	}
	
	public List<Booking> getAllBooking(User user,Boolean isOpen){
		return bookingComponent.getAllBookingByUser(user,isOpen);
	}
}
