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
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.TransferRequest;
import com.mindfire.bicyclesharing.model.TransferResponse;

/**
 * Repository for {@link TransferResponse} Entity used for CRUD operation on
 * TransferResponse.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface TransferResponseRepository extends JpaRepository<TransferResponse, Long> {

	/**
	 * This method is used to retrieve responses to transfer requests from a
	 * specific pickup point
	 * 
	 * @param pickUpPoint
	 *            concerned pickup point
	 * @return {@link TransferResponse} List
	 */
	public List<TransferResponse> findByPickUpPoint(PickUpPoint pickUpPoint);

	/**
	 * This method is used to retrieve responses to a specific transfer request.
	 * 
	 * @param request
	 *            concerned transfer request
	 * @return {@link TransferResponse} List
	 */
	public List<TransferResponse> findByRequest(TransferRequest request);

	/**
	 * This method is used to retrieve details of a response from its id.
	 * 
	 * @param responseId
	 *            id of the transfer response
	 * @return {@link TransferResponse} object
	 */
	public TransferResponse findByResponseId(Long responseId);

	/**
	 * This method is used to retrieve record of a transfer response from a
	 * specific pickup point to a specific request.
	 * 
	 * @param request
	 *            the transfer request
	 * @param pickUpPoint
	 *            the concerned pickup point
	 * @return {@link TransferResponse} object
	 */
	public Optional<TransferResponse> findByRequestAndPickUpPoint(TransferRequest request, PickUpPoint pickUpPoint);

	/**
	 * This method is used to update the status of the response
	 * 
	 * @param isApproved
	 *            status <code>true</code> or <code>false</code>
	 * @param responseId
	 *            id of the response
	 * @return {@link Integer} 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update TransferResponse ts set ts.isApproved =:isApproved where ts.responseId =:responseId")
	public int updateIsApproved(@Param("isApproved") Boolean isApproved, @Param("responseId") long responseId);
}
