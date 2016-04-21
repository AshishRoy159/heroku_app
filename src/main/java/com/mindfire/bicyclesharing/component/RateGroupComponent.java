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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.RateGroupDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.RateGroup;
import com.mindfire.bicyclesharing.model.User;
import com.mindfire.bicyclesharing.repository.BaseRateRepository;
import com.mindfire.bicyclesharing.repository.RateGroupRepository;

/**
 * RateGroupComponent class is used for interacting with corresponding
 * repository class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class RateGroupComponent {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private RateGroupRepository rateGroupRepository;

	@Autowired
	private BaseRateRepository baseRateRepository;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * This method is used to find the rate group by groupType
	 * 
	 * @param user
	 *            User object
	 * @return {@link RateGroup} object
	 */
	public RateGroup mapRateGroup(User user) {
		return rateGroupRepository.findByGroupTypeAndIsActive(user.getRateGroup(), true);
	}

	/**
	 * This method is used to add add new rate group.
	 * 
	 * @param rateGroupDTO
	 *            the incoming data to be saved
	 * @return {@link RateGroup} object
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	public RateGroup mapNewRateGroupDetails(RateGroupDTO rateGroupDTO) throws ParseException {
		RateGroup existingRateGroup = rateGroupRepository.findByGroupTypeAndIsActive(rateGroupDTO.getGroupType(), true);
		RateGroup rateGroup = new RateGroup();

		rateGroup.setDiscount(rateGroupDTO.getDiscount());
		rateGroup.setEffectiveFrom(simpleDateFormat.parse(rateGroupDTO.getEffectiveFrom()));
		rateGroup.setGroupType(rateGroupDTO.getGroupType());
		rateGroup.setBaseRateBean(baseRateRepository.findByGroupType("USER"));

		if (null == existingRateGroup) {
			rateGroup.setIsActive(true);
		} else {
			rateGroup.setIsActive(false);
		}
		RateGroup saveRateGroup =new RateGroup();

		try {
			logger.info("Created new rate group data.");
		saveRateGroup =	rateGroupRepository.save(rateGroup);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return null;
		}
		return saveRateGroup;
	}

	/**
	 * This method is used to find all Rate Group
	 * 
	 * @return {@link RateGroup} List
	 */
	public List<RateGroup> getAllRateGroup() {
		return rateGroupRepository.findAll();
	}

	/**
	 * This method is used to find all rate group based on isActive.
	 * 
	 * @param isActive
	 *            Boolean value
	 * @return {@link RateGroup} List
	 */
	public List<RateGroup> mapAllRateGroupAndIsActive(Boolean isActive) {
		return rateGroupRepository.findAllByIsActive(isActive);
	}

	/**
	 * This method is used to find RateGroup by id.
	 * 
	 * @param id
	 *            rateGroupId
	 * @return {@link RateGroup} object
	 */
	public RateGroup mapRateGroupById(Integer id) {
		return rateGroupRepository.findByRateGroupId(id);
	}

	/**
	 * This method is used to check year is leap year or not.
	 * 
	 * @param year
	 *            this is year value in integer
	 * @return {@link Boolean} value
	 */
	public Boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is used to check number of days in the corresponding month.
	 * 
	 * @param month
	 *            this is integer value for month
	 * @return {@link Integer} value
	 */
	public int checkMonth(int month) {
		int day = 0;

		switch (month) {
		case 0:
			day = 31;
			break;
		case 1:
			day = 28;
			break;
		case 2:
			day = 31;
			break;
		case 3:
			day = 30;
			break;
		case 4:
			day = 31;
			break;
		case 5:
			day = 30;
			break;
		case 6:
			day = 31;
			break;
		case 7:
			day = 31;
			break;
		case 8:
			day = 30;
			break;
		case 9:
			day = 31;
			break;
		case 10:
			day = 30;
			break;
		case 11:
			day = 31;
			break;
		}
		return day;
	}

	/**
	 * This method is used to update rate group type and create new rate group
	 * type.
	 * 
	 * @param rateGroupDTO
	 *            this object contains rate group related data.
	 * @return {@link RateGroup} object
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	public String mapUpdateRateGroupAndIsActive(RateGroupDTO rateGroupDTO) throws ParseException {
		RateGroup existingRateGroup = rateGroupRepository
				.findByGroupTypeAndIsActiveAndEffectiveUptoIsNull(rateGroupDTO.getGroupType(), false);

		if (null != existingRateGroup) {
			/* Some more work will be done here.. */
			existingRateGroup.setEffectiveFrom(simpleDateFormat.parse(rateGroupDTO.getEffectiveFrom()));
			existingRateGroup.setDiscount(rateGroupDTO.getDiscount());

			try {
				rateGroupRepository.save(existingRateGroup);
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
			}
			return updateEffectiveUpTo(existingRateGroup);
		} else {
			RateGroup updateRateGroup = rateGroupRepository
					.findByGroupTypeAndIsActiveAndEffectiveUptoIsNull(rateGroupDTO.getGroupType(), true);

			if (updateRateGroup.getEffectiveFrom().after(simpleDateFormat.parse(simpleDateFormat.format(new Date())))) {
				updateRateGroup.setDiscount(rateGroupDTO.getDiscount());
				updateRateGroup.setEffectiveFrom(simpleDateFormat.parse(rateGroupDTO.getEffectiveFrom()));

				try {
					 rateGroupRepository.save(updateRateGroup);
				} catch (DataIntegrityViolationException dataIntegrityViolationException) {
					return "Duplicate Data";
				}
			}
			RateGroup newRateGroup = mapNewRateGroupDetails(rateGroupDTO);
			return updateEffectiveUpTo(newRateGroup);
		}
	}

	/**
	 * This method is used to update the effective upto field of the rate group.
	 * 
	 * @param newRateGroup
	 *            RateGroup object
	 * @return {@link RateGroup} object
	 * @throws ParseException
	 *             may occur while parsing from String to Date
	 */
	public String updateEffectiveUpTo(RateGroup newRateGroup) throws ParseException {
		RateGroup rateGroup = rateGroupRepository.findByGroupTypeAndIsActive(newRateGroup.getGroupType(), true);
		@SuppressWarnings("deprecation")
		int date = newRateGroup.getEffectiveFrom().getDate();

		// it will return 0 to 11
		@SuppressWarnings("deprecation")
		int month = newRateGroup.getEffectiveFrom().getMonth();

		@SuppressWarnings("deprecation")
		int year = newRateGroup.getEffectiveFrom().getYear() + 1900;

		if (date == 1 && month == 0) {
			rateGroup.setEffectiveUpto(simpleDateFormat
					.parse(String.valueOf(year - 1) + "-" + String.valueOf(12) + "-" + String.valueOf(31)));
		} else if (date == 1 && month == 2) {

			if (isLeapYear(year)) {
				rateGroup.setEffectiveUpto(simpleDateFormat
						.parse(String.valueOf(year) + "-" + String.valueOf(2) + "-" + String.valueOf(29)));
			} else {
				rateGroup.setEffectiveUpto(simpleDateFormat
						.parse(String.valueOf(year) + "-" + String.valueOf(2) + "-" + String.valueOf(28)));
			}
		} else {

			if (date == 1) {
				rateGroup.setEffectiveUpto(simpleDateFormat.parse(
						String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(checkMonth(month))));
			} else {
				rateGroup.setEffectiveUpto(simpleDateFormat.parse(
						String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(date - 1)));
			}
		}

		try {
			logger.info("Updated rate group deatils.");
		 rateGroupRepository.save(rateGroup);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return "Duplicate data";
		}
		return "Successfully Updated !";
	}
}
