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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.Booking;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.repository.BookingRepository;
import com.mindfire.bicyclesharing.repository.RateGroupRepository;

/**
 * ScheduledTasksComponent class is used for interacting with corresponding
 * repository class based on the specific time scheduled.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class ScheduledTasksComponent {

	Logger logger = Logger.getLogger(getClass());

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private RateGroupRepository rateGroupRepository;

	@Autowired
	private BookingRepository bookingRepository;

	/**
	 * This method is used to update the rate group table in database on the
	 * given time scheduled. This method execute everyday at 23h 59m and 58s.
	 * 
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	@Scheduled(cron = "58 59 23 * * *")
	public void updateDataBase() throws ParseException {
		List<RateGroup> rateGroups = rateGroupRepository.findAllByIsActiveAndEffectiveUptoIsNotNull(true);

		for (RateGroup rateGroup : rateGroups) {

			if (rateGroup.getEffectiveUpto().equals(dateFormat.parse(dateFormat.format(new Date())))) {
				RateGroup newRateGroup = rateGroupRepository
						.findByGroupTypeAndIsActiveAndEffectiveUptoIsNull(rateGroup.getGroupType(), false);
				rateGroup.setIsActive(false);

				try {
					rateGroupRepository.save(rateGroup);
					logger.info("Old rate group status is set to inactive.");
				} catch (DataIntegrityViolationException dataIntegrityViolationException) {
					throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
				}
				newRateGroup.setIsActive(true);

				try {
					rateGroupRepository.save(newRateGroup);
					logger.info("New rate group status is set to active.");
				} catch (DataIntegrityViolationException dataIntegrityViolationException) {
					throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
				}
			}
		}

		// This is used to updating the booking table.
		List<Booking> bookings = bookingRepository.findAllByIsOpenAndBiCycleIdIsNull(true);

		for (Booking booking : bookings) {

			if (booking.getExpectedIn().before(new Timestamp(System.currentTimeMillis()))) {
				booking.setIsOpen(false);
				booking.setIsUsed(false);

				try {
					bookingRepository.save(booking);
				} catch (DataIntegrityViolationException dataIntegrityViolationException) {
					throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
				}
			}
		}
		logger.info("All unused bookings are set to closed.");
	}
}
