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

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindfire.bicyclesharing.model.ProofDetail;

/**
 * Repository for {@link ProofDetail} Entity used for CRUD operation on
 * ProofDetail.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface ProofDetailRepository extends JpaRepository<ProofDetail, Long> {

	/**
	 * This method is used to get the user proof details by the proof no.
	 * @param proofNo
	 *            the user's document proof no.
	 * @return {@link ProofDetail} object
	 */
	public Optional<ProofDetail> findByProofNo(String proofNo);
}
