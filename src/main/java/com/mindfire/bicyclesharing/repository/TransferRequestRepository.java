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

package com.mindfire.bicyclesharing.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferRequest;

/**
 * Repository for {@link TransferRequest} Entity used for CRUD operation on
 * TransferRequest.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface TransferRequestRepository extends DataTablesRepository<TransferRequest, Long> {

	/**
	 * This method is used to retrieve all transfer requests from the database
	 * 
	 * @return {@link TransferRequest} List
	 */
	public List<TransferRequest> findAll();

	/**
	 * this method is used retrieve transfer request details from its id.
	 * 
	 * @param requestId
	 *            the id of the {@link TransferRequest} object
	 * @return {@link TransferRequest} object
	 */
	public TransferRequest findByRequestId(Long requestId);

	/**
	 * This method is used to retrieve transfer requests from pickup points
	 * other than the current one.
	 * 
	 * @param isApproved
	 *            status of the request
	 * @param pickUpPoint
	 *            the current pickup point
	 * @return {@link TransferRequest} List
	 */
	public List<TransferRequest> findByIsApprovedAndPickUpPointNot(Boolean isApproved, PickUpPoint pickUpPoint);

	/**
	 * This method is used to update the approvedQuantity field
	 * 
	 * @param approvedQuantity
	 *            the new approvedQuantity
	 * @param requestId
	 *            id of the request
	 * @return Integer 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update TransferRequest tr set tr.approvedQuantity = :approvedQuantity where tr.requestId = :requestId")
	public int updateCurrentApprovedQuantity(@Param("approvedQuantity") Integer approvedQuantity,
			@Param("requestId") Long requestId);

	/**
	 * This method is update the status of a transfer request
	 * 
	 * @param isApproved
	 *            status <code>true</code> or <code>false</code>
	 * @param requestId
	 *            id of the request
	 * @return {@link Integer} 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update TransferRequest tr set tr.isApproved = :isApproved where tr.requestId = :requestId")
	public int updateIsApproved(@Param("isApproved") Boolean isApproved, @Param("requestId") Long requestId);

	/**
	 * This method is used to retrieve transfer requests by approval status
	 * 
	 * @param isApproved
	 *            approval status
	 * @return {@link TransferRequest} List
	 */
	public List<TransferRequest> findByIsApproved(Boolean isApproved);
}
