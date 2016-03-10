package com.mindfire.bicyclesharing.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the rate_groups database table.
 * 
 */
@Entity
@Table(name = "rate_groups")
@NamedQuery(name = "RateGroup.findAll", query = "SELECT r FROM RateGroup r")
public class RateGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rate_group_id")
	private Integer rateGroupId;

	@Column(name = "base_rate")
	private double baseRate;

	private double discount;

	@Temporal(TemporalType.DATE)
	@Column(name = "effective_from")
	private Date effectiveFrom;

	@Temporal(TemporalType.DATE)
	@Column(name = "effective_upto")
	private Date effectiveUpto;

	@Column(name = "group_type")
	private String groupType;

	public RateGroup() {
	}

	/**
	 * @return the rateGroupId
	 */
	public Integer getRateGroupId() {
		return rateGroupId;
	}

	/**
	 * @param rateGroupId
	 *            the rateGroupId to set
	 */
	public void setRateGroupId(Integer rateGroupId) {
		this.rateGroupId = rateGroupId;
	}

	/**
	 * @return the baseRate
	 */
	public double getBaseRate() {
		return baseRate;
	}

	/**
	 * @param baseRate
	 *            the baseRate to set
	 */
	public void setBaseRate(double baseRate) {
		this.baseRate = baseRate;
	}

	/**
	 * @return the discount
	 */
	public double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @return the effectiveFrom
	 */
	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	/**
	 * @param effectiveFrom
	 *            the effectiveFrom to set
	 */
	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	/**
	 * @return the effectiveUpto
	 */
	public Date getEffectiveUpto() {
		return effectiveUpto;
	}

	/**
	 * @param effectiveUpto
	 *            the effectiveUpto to set
	 */
	public void setEffectiveUpto(Date effectiveUpto) {
		this.effectiveUpto = effectiveUpto;
	}

	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}

	/**
	 * @param groupType
	 *            the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
}
