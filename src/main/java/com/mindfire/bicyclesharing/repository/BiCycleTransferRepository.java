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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindfire.bicyclesharing.model.BiCycle;
import com.mindfire.bicyclesharing.model.BiCycleTransfer;
import com.mindfire.bicyclesharing.model.Transfer;

/**
 * Repository for {@link BiCycle} Entity used for CRUD operation on BiCycle.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface BiCycleTransferRepository extends JpaRepository<BiCycleTransfer, Long> {

	/**
	 * This method is used to retrieve the bicycles dispatched on a specific
	 * transfer.
	 * 
	 * @param transfer
	 *            concerned transfer
	 * @return {@link BiCycleTransfer} List
	 */
	public List<BiCycleTransfer> findByTransfer(Transfer transfer);
}
