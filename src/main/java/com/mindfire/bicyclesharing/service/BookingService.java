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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mindfire.bicyclesharing.component.BookingComponent;
import com.mindfire.bicyclesharing.component.RateGroupComponent;
import com.mindfire.bicyclesharing.component.UserBookingComponent;
import com.mindfire.bicyclesharing.dto.BookingPaymentDTO;
import com.mindfire.bicyclesharing.dto.IssueCycleDTO;
import com.mindfire.bicyclesharing.dto.IssueCycleForOnlineDTO;
import com.mindfire.bicyclesharing.dto.PaymentAtPickUpPointDTO;
import com.mindfire.bicyclesharing.dto.ReceiveBicyclePaymentDTO;
import com.mindfire.bicyclesharing.dto.UserBookingDTO;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.model.Wallet;
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

	@Autowired
	private RateGroupComponent rateGroupComponent;

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
	 * @param input
	 *            {@link DataTablesInput} object
	 * @param user
	 *            User object
	 * @param isUsed
	 *            boolean value
	 * @return {@link Booking} List
	 */
	public DataTablesOutput<Booking> getAllBooking(DataTablesInput input, User user, Boolean isUsed) {
		return bookingComponent.getAllBookingByUser(input, user, isUsed);
	}

	/**
	 * This method is used for getting all booking details based on booking
	 * status.
	 * 
	 * @param isUsed
	 *            this is Boolean value
	 * @param input
	 *            {@link DataTablesInput} object
	 * @return {@link Booking} {@link DataTablesOutput}
	 */
	public DataTablesOutput<Booking> getAllBookingDetails(DataTablesInput input, Boolean isUsed) {
		return bookingComponent.getAllBookings(input, isUsed);
	}

	/**
	 * This method is used to close unused booking.
	 * 
	 * @param booking
	 *            current booking object
	 * @return {@link Booking}
	 */
	public Booking closeBooking(Booking booking) {
		return bookingComponent.mapCloseBooking(booking);
	}

	/**
	 * This method is used to get the user booking details.
	 * 
	 * @param isOpen
	 *            Boolean value
	 * @param user
	 *            User object
	 * @return {@link Booking} object
	 */
	public Booking getBookingDetails(Boolean isOpen, User user) {
		return bookingComponent.mapExistingBooking(isOpen, user);
	}

	/**
	 * This method calculates the expected return time in timestamp format
	 * 
	 * @param issueCycleDTO
	 *            issue cycle data
	 * @return {@link Calendar} object
	 */
	public Calendar calculateExpectedIn(IssueCycleDTO issueCycleDTO) {
		final Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(new Date().getTime());
		cal.add(Calendar.HOUR, issueCycleDTO.getExpectedInTime());
		return cal;
	}

	/**
	 * This method calculates the fare depending on the user rate group and
	 * expected in time
	 * 
	 * @param user
	 *            user data
	 * @param hour
	 *            booking duration
	 * @return {@link Double} object
	 */
	public Double calculateFare(User user, long hour) {
		RateGroup rateGroup = rateGroupComponent.mapRateGroup(user);
		return (rateGroup.getBaseRateBean().getBaseRate() * hour);
	}

	/**
	 * This method is used to calculate discount.
	 * 
	 * @param user
	 *            User object
	 * @param fare
	 *            riding fare
	 * @return {@link Double} value
	 */
	public Double calculateDiscount(User user, Double fare) {
		RateGroup rateGroup = rateGroupComponent.mapRateGroup(user);
		return (fare * (rateGroup.getDiscount() / 100));
	}

	/**
	 * This method is used to calculate the riding time based on the booking.
	 * 
	 * @param booking
	 *            this is Booking object
	 * @return {@link Long} value
	 */
	public long calculateRidingTime(Booking booking) {
		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(new Date().getTime());
		return cal.getTimeInMillis() - booking.getExpectedIn().getTime();
	}

	/**
	 * This method is used to calculate total riding time based on their actual
	 * time.
	 * 
	 * @param actualTime
	 *            this is actual time (difference between expectedIn and
	 *            expectedOut)
	 * @return {@link Long} value
	 */
	public long calculateTotalRideTime(long actualTime) {
		long hour = (actualTime / (60 * 1000)) / 60;
		long remainder = (actualTime / (60 * 1000)) % 60;

		if (remainder > 0) {
			hour++;
		}
		return hour;
	}

	/**
	 * This method is used to calculate actual fare based on the base rate and
	 * time.
	 * 
	 * @param hour
	 *            this is riding time
	 * @param baseRate
	 *            this is base rate for riding per hour
	 * @param discount
	 *            the discuont rate of the rate group
	 * @return {@link Double} value
	 */
	public double calculateActualFare(long hour, double baseRate, double discount) {
		return (hour * (baseRate + ((baseRate * 10) / 100)) - (baseRate * (discount / 100)));
	}

	/**
	 * This method is used to save the receive bicycle payment details.
	 * 
	 * @param receiveBicyclePaymentDTO
	 *            receive bicycle payment data
	 * @param userWallet
	 *            this is wallet object associated to the user
	 * @return {@link WalletTransaction} object
	 */
	public WalletTransaction saveReceiveBicyclePayment(ReceiveBicyclePaymentDTO receiveBicyclePaymentDTO,
			Wallet userWallet) {
		return bookingComponent.mapReceiveBicyclePayment(receiveBicyclePaymentDTO, userWallet);
	}

	/**
	 * This method is used to get all running bookings.
	 * 
	 * @param isOpen
	 *            Boolean value this is used to check the booking status
	 * @return {@link Booking} List
	 */
	public List<Booking> getRunningBooking(Boolean isOpen) {
		return bookingComponent.mapRunningBooking(isOpen);
	}
}
