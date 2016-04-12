package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the base_rate database table.
 * 
 */
@Entity
@Table(name="base_rate")
@NamedQuery(name="BaseRate.findAll", query="SELECT b FROM BaseRate b")
public class BaseRate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="base_rate_id")
	private Long baseRateId;

	@Column(name="base_rate")
	private double baseRate;

	@Column(name="group_type")
	private String groupType;

	//bi-directional many-to-one association to RateGroup
	@OneToMany(mappedBy="baseRateBean")
	private List<RateGroup> rateGroups;

	public BaseRate() {
	}

	public Long getBaseRateId() {
		return this.baseRateId;
	}

	public void setBaseRateId(Long baseRateId) {
		this.baseRateId = baseRateId;
	}

	public double getBaseRate() {
		return this.baseRate;
	}

	public void setBaseRate(double baseRate) {
		this.baseRate = baseRate;
	}

	public String getGroupType() {
		return this.groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public List<RateGroup> getRateGroups() {
		return this.rateGroups;
	}

	public void setRateGroups(List<RateGroup> rateGroups) {
		this.rateGroups = rateGroups;
	}

	public RateGroup addRateGroup(RateGroup rateGroup) {
		getRateGroups().add(rateGroup);
		rateGroup.setBaseRateBean(this);

		return rateGroup;
	}

	public RateGroup removeRateGroup(RateGroup rateGroup) {
		getRateGroups().remove(rateGroup);
		rateGroup.setBaseRateBean(null);

		return rateGroup;
	}

}