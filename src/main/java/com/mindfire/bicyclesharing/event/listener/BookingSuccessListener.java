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

package com.mindfire.bicyclesharing.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.event.BookingSuccessEvent;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.service.SMSService;

/**
 * RegistrationListener is an event listener for OnRegistrationCompleteEvent
 * event class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class BookingSuccessListener implements ApplicationListener<BookingSuccessEvent> {

	@Autowired
	private SMSService smsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationListener#onApplicationEvent(org.
	 * springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(BookingSuccessEvent event) {
		try {
			this.confirmBooking(event);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method is used for creating verification token, construct and send
	 * the verification email
	 * 
	 * @param event
	 *            OnRegistrationCompleteEvent
	 * @throws Exception
	 */
	private void confirmBooking(final BookingSuccessEvent event) throws Exception {

		final User user = event.getUser();
		final String recipientNumber = "91" + user.getMobileNo();
		final Long bookingId = event.getBooking().getBookingId();
		final String message = "Hello " + user.getFirstName() + "." + "\n"
				+ "Your booking is successful. Your Booking Id is " + bookingId + ". Thank you for using our Service.";

		smsService.sendMessage(recipientNumber, message);
	}
}
