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

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindfire.bicyclesharing.dto.BiCycleDTO;
import com.mindfire.bicyclesharing.exception.CustomException;
import com.mindfire.bicyclesharing.exception.ExceptionMessages;
import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.BiCycleTransfer;
import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Transfer;
import com.mindfire.bicyclesharing.repository.BiCycleRepository;
import com.mindfire.bicyclesharing.repository.BiCycleTransferRepository;
import com.mindfire.bicyclesharing.repository.PickUpPointRepository;

/**
 * BiCycleComponent class is used to get the data from the BiCycleDTO class and
 * set the data to the corresponding Entity class
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Component
public class BiCycleComponent {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PickUpPointRepository pickUpPointRepository;

	@Autowired
	private PickUpPointComponent pickUpPointComponent;

	@Autowired
	private BiCycleRepository biCycleRepository;

	@Autowired
	private BiCycleTransferRepository biCycleTransferRepository;

	/**
	 * This method is used for receiving the data from BiCycleDTO object and set
	 * the data to the corresponding entity class
	 * 
	 * @param biCycleDTO
	 *            the data from the view
	 * @return BiCycle object
	 */
	public String mapBiCycleData(BiCycleDTO biCycleDTO) {
		BiCycle biCycle = new BiCycle();
		PickUpPoint pickUpPoint = pickUpPointRepository.findByPickUpPointId(biCycleDTO.getPickUpPoint());

		if (pickUpPoint.getMaxCapacity() > pickUpPoint.getCurrentAvailability()) {
			logger.info("There is space available for new bicycle.");
			biCycle.setChasisNo(biCycleDTO.getChasisNo());
			biCycle.setCurrentLocation(pickUpPoint);

			try {
				biCycleRepository.save(biCycle);
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				logger.error("Bicycle with the chasis number already exists.");
				return "duplicate data";
			}
			pickUpPointComponent.updateBiCycleCurrentAvailability(pickUpPoint,
					biCycleRepository.findByCurrentLocationAndIsAvailable(pickUpPoint, true).size());
			logger.info("Bicycle with chassis no. " + biCycleDTO.getChasisNo() + " added to database.");
			return "Successfully Added!!!";
		} else {
			logger.info("There are no more spaces for new bicycle at the desired pickup point.");
			return null;
		}
	}

	/**
	 * This method is used to find a certain number of available bicycles on a
	 * specific pickup point
	 * 
	 * @param pickUpPoint
	 *            the concerned pickup point
	 * @param isAvailable
	 *            availability of bicycle. true or false
	 * @param pageable
	 *            the number of record to be fetched
	 * @return {@link BiCycle} List
	 */
	public List<BiCycle> getAvailableBicycles(PickUpPoint pickUpPoint, Boolean isAvailable, Pageable pageable) {
		return biCycleRepository.findByCurrentLocationAndIsAvailableOrderByChasisNoAsc(pickUpPoint, isAvailable,
				pageable);
	}

	/**
	 * This method is used set the bicycles to be transfered.
	 * 
	 * @param session
	 *            {@link HttpSession} object to get the list of bicycles
	 *            selected for transfer
	 * @param transfer
	 *            the current transfer in transition
	 */
	public void bicyclesInTransition(HttpSession session, Transfer transfer) {
		@SuppressWarnings("unchecked")
		List<BiCycle> biCycles = (List<BiCycle>) session.getAttribute("biCycles");

		for (BiCycle biCycle : biCycles) {
			BiCycleTransfer biCycleTransfer = new BiCycleTransfer();

			biCycle.setIsAvailable(false);

			try {
				biCycleRepository.save(biCycle);
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
			}
			biCycleTransfer.setBiCycle(biCycle);
			biCycleTransfer.setTransfer(transfer);

			try {
				biCycleTransferRepository.save(biCycleTransfer);
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/**
	 * This method is used to change the location of the bicycles after they are
	 * transferred.
	 * 
	 * @param session
	 *            the {@link HttpSession} object to get list of bicycles
	 *            received
	 * @param transfer
	 *            the current transfer in transition
	 */
	public void bicyclesTransferred(HttpSession session, Transfer transfer) {
		@SuppressWarnings("unchecked")
		List<BiCycle> biCycles = (List<BiCycle>) session.getAttribute("biCycles");

		for (BiCycle biCycle : biCycles) {
			biCycle.setIsAvailable(true);
			biCycle.setCurrentLocation(transfer.getTransferredTo());

			try {
				biCycleRepository.save(biCycle);
			} catch (DataIntegrityViolationException dataIntegrityViolationException) {
				throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/**
	 * This method is used to get the number of available bicycles on a specific
	 * pickup point
	 * 
	 * @param pickUpPoint
	 *            the concerned pickup point
	 * @param isAvailable
	 *            availability of bicycle. True of false
	 * @return {@link Integer} the quantity
	 */
	public int findByCurrentLocationAndIsAvailable(PickUpPoint pickUpPoint, Boolean isAvailable) {
		return biCycleRepository.findByCurrentLocationAndIsAvailable(pickUpPoint, true).size();
	}

	/**
	 * This method is used to get the bicycles on a specific pickup point
	 * 
	 * @param pickUpPoint
	 *            the concerned pickup point
	 * @param isAvailable
	 *            availability of bicycle. True of false
	 * @return {@link BiCycle} List
	 */
	public List<BiCycle> findByCurrentLocationAndAvailability(PickUpPoint pickUpPoint, Boolean isAvailable) {
		return biCycleRepository.findByCurrentLocationAndIsAvailable(pickUpPoint, true);
	}

	/**
	 * This method is used to update the bicycle status.
	 * 
	 * @param id
	 *            bicycle id
	 * @return {@link BiCycle} object
	 */
	public BiCycle updateBicycle(Long id) {
		BiCycle biCycle = biCycleRepository.findByBiCycleId(id);

		biCycle.setIsAvailable(false);

		try {
			biCycleRepository.save(biCycle);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw new CustomException(ExceptionMessages.DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
		}
		logger.info("Bicycle status updated to unavailable.");
		return biCycle;
	}
}
