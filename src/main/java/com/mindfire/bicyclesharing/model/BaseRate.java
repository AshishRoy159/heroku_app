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

package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the base_rate database table.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Entity
@Table(name = "base_rate")
@NamedQuery(name = "BaseRate.findAll", query = "SELECT b FROM BaseRate b")
public class BaseRate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "base_rate_id")

	private Long baseRateId;

	@Column(name = "base_rate")
	private double baseRate;

	@Column(name = "group_type", unique = true)
	private String groupType;

	// bi-directional many-to-one association to RateGroup
	@OneToMany(mappedBy = "baseRateBean")
	private List<RateGroup> rateGroups;

	/**
	 * the default constructor
	 */
	public BaseRate() {
	}

	/**
	 * @return the baseRateId
	 */
	public Long getBaseRateId() {
		return this.baseRateId;
	}

	/**
	 * @param baseRateId
	 *            the baseRateId to set
	 */
	public void setBaseRateId(Long baseRateId) {
		this.baseRateId = baseRateId;
	}

	/**
	 * @return the baseRate value
	 */
	public double getBaseRate() {
		return this.baseRate;
	}

	/**
	 * @param baseRate
	 *            the baseRate value to set
	 */
	public void setBaseRate(double baseRate) {
		this.baseRate = baseRate;
	}

	/**
	 * @return the group type
	 */
	public String getGroupType() {
		return this.groupType;
	}

	/**
	 * @param groupType
	 *            the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return {@link RateGroup} list
	 */
	public List<RateGroup> getRateGroups() {
		return this.rateGroups;
	}

	/**
	 * @param rateGroups
	 *            the rateGroup list to set
	 */
	public void setRateGroups(List<RateGroup> rateGroups) {
		this.rateGroups = rateGroups;
	}

	/**
	 * @param rateGroup
	 *            the rateGroup object
	 * @return {@link RateGroup} object
	 */
	public RateGroup addRateGroup(RateGroup rateGroup) {
		getRateGroups().add(rateGroup);
		rateGroup.setBaseRateBean(this);

		return rateGroup;
	}

	/**
	 * @param rateGroup
	 *            the rateGroup object
	 * @return {@link RateGroup} object
	 */
	public RateGroup removeRateGroup(RateGroup rateGroup) {
		getRateGroups().remove(rateGroup);
		rateGroup.setBaseRateBean(null);

		return rateGroup;
	}

}