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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mindfire.bicyclesharing.model.PickUpPoint;

/**
 * Repository for {@link PickUpPoint} Entity used for CRUD operation on
 * PickUpPoint.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Repository
public interface PickUpPointRepository extends JpaRepository<PickUpPoint, Integer> {

	/**
	 * This method is used to get the details of all pickup points
	 * 
	 * @return PickUpPoint list
	 */
	public List<PickUpPoint> findAllByOrderByPickUpPointIdAsc();

	/**
	 * This method is used to get the details of all active pickup points
	 * 
	 * @param isActive
	 *            true or false
	 * @return PickUpPoint list
	 */
	public List<PickUpPoint> findByIsActiveOrderByPickUpPointIdAsc(boolean isActive);

	/**
	 * This method is used to find the details of a specific pickup point by its
	 * id
	 * 
	 * @param pickUpPointId
	 *            id of a pickup point
	 * @return PickUpPoint object
	 */
	public PickUpPoint findByPickUpPointId(Integer pickUpPointId);

	/**
	 * This method is used for updating particular pickup point details.
	 * 
	 * @param location
	 *            of the pickup point
	 * @param maxCapacity
	 *            of the pickup point
	 * @param isActive
	 *            status of the pickup point
	 * @param pickUpPointId
	 *            of the pickup point
	 * @return Integer 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update PickUpPoint p set p.location =:location, p.maxCapacity =:maxCapacity, p.isActive=:isActive where p.pickUpPointId =:pickUpPointId")
	public int updatePickUpPoint(@Param("location") String location, @Param("maxCapacity") int maxCapacity,
			@Param("isActive") Boolean isActive, @Param("pickUpPointId") int pickUpPointId);

	/**
	 * This method is used for updating the current availability of bicycle at
	 * particular pickup point.
	 * 
	 * @param currentAvailability
	 *            of the pickup point
	 * @param pickUpPointId
	 *            of the pickup point
	 * @return Integer 0 or 1
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update PickUpPoint p set p.currentAvailability =:currentAvailability where p.pickUpPointId =:pickUpPointId")
	public int updateCurrentAvailability(@Param("currentAvailability") int currentAvailability,
			@Param("pickUpPointId") int pickUpPointId);
}
