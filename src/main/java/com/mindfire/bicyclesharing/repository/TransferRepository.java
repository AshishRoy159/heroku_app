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
import org.springframework.stereotype.Repository;

import com.mindfire.bicyclesharing.model.PickUpPoint;
import com.mindfire.bicyclesharing.model.Transfer;

/**
 * Repository for {@link Transfer} Entity used for CRUD operation on Transfer.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface TransferRepository extends DataTablesRepository<Transfer, Long> {

	/**
	 * This method is retrieve the transfer details from its id
	 * 
	 * @param transferId
	 *            id of the transfer
	 * @return {@link Transfer} object
	 */
	public Transfer findByTransferId(Long transferId);

	/**
	 * This method is used to retrieve details of transfers from a pickup point.
	 * 
	 * @param transferredFrom
	 *            concerned pickup point
	 * @return {@link Transfer} List
	 */
	public List<Transfer> findByTransferredFrom(PickUpPoint transferredFrom);

	/**
	 * This method is used to retrieve details of transfers from a pickup point.
	 * 
	 * @param transferredTo
	 *            concerned pickup point
	 * @return {@link Transfer} List
	 */
	public List<Transfer> findByTransferredTo(PickUpPoint transferredTo);
}
