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

import com.mindfire.bicyclesharing.model.RateGroup;

/**
 * Repository for {@link RateGroup} Entity used for CRUD operation on RateGroup.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface RateGroupRepository extends JpaRepository<RateGroup, Long> {

	/**
	 * This method is used to get a RateGroup detail by groupType
	 * 
	 * @param rateGroup
	 *            the groupType
	 * @param isActive
	 *            true or false
	 * @return RateGroup object
	 */
	public RateGroup findByGroupTypeAndIsActive(String rateGroup, Boolean isActive);

	/**
	 * This method is used to get the details of all rate groups.
	 */
	public List<RateGroup> findAll();

	/**
	 * This method is used to get the details of all active rate groups.
	 * 
	 * @param isActive
	 *            true or false
	 * @return {@link RateGroup} List
	 */
	public List<RateGroup> findAllByIsActive(Boolean isActive);

	/**
	 * This method is used to find the rate groups based on isActive and
	 * effective upto fields.
	 * 
	 * @param isActive
	 *            true or false
	 * @return {@link RateGroup} List
	 */
	public List<RateGroup> findAllByIsActiveAndEffectiveUptoIsNotNull(Boolean isActive);

	/**
	 * This method is used to find a particular rate group.
	 * 
	 * @param id
	 *            rate group id
	 * @return {@link RateGroup}
	 */
	public RateGroup findByRateGroupId(Integer id);

	/**
	 * This method is used to find a rate group based on group type,Is active
	 * and effective Upto feilds.
	 * 
	 * @param groupType
	 *            the groupType
	 * @param isActive
	 *            true or false
	 * @return {@link RateGroup}
	 */
	public RateGroup findByGroupTypeAndIsActiveAndEffectiveUptoIsNull(String groupType, Boolean isActive);
}
