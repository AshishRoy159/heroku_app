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

	private double discount;

	@Temporal(TemporalType.DATE)
	@Column(name = "effective_from")
	private Date effectiveFrom;

	@Temporal(TemporalType.DATE)
	@Column(name = "effective_upto")
	private Date effectiveUpto;

	@Column(name = "group_type")
	private String groupType;

	// bi-directional many-to-one association to BaseRate
	@ManyToOne
	@JoinColumn(name = "base_rate")
	private BaseRate baseRateBean;

	@Column(name = "is_active")
	private Boolean isActive;

	/**
	 * the default constructor
	 */
	public RateGroup() {
	}

	/**
	 * @return the rateGroupId
	 */
	public Integer getRateGroupId() {
		return this.rateGroupId;
	}

	/**
	 * @param rateGroupId
	 *            the rateGroupId to set
	 */
	public void setRateGroupId(Integer rateGroupId) {
		this.rateGroupId = rateGroupId;
	}

	/**
	 * @return the discount value
	 */
	public double getDiscount() {
		return this.discount;
	}

	/**
	 * @param discount
	 *            the discount value to set
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @return the effectiveDateFrom value
	 */
	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	/**
	 * @param effectiveFrom
	 *            the effectiveDateFrom value to set
	 */
	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	/**
	 * @return to set the effectiveUpto value
	 */
	public Date getEffectiveUpto() {
		return this.effectiveUpto;
	}

	/**
	 * @param effectiveUpto
	 *            the effectiveUpto value
	 */
	public void setEffectiveUpto(Date effectiveUpto) {
		this.effectiveUpto = effectiveUpto;
	}

	/**
	 * @return the groupType value
	 */
	public String getGroupType() {
		return this.groupType;
	}

	/**
	 * @param groupType
	 *            the groupType value to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return {@link BaseRate} object
	 */
	public BaseRate getBaseRateBean() {
		return this.baseRateBean;
	}

	/**
	 * @param baseRateBean
	 *            the baseRate object to set
	 */
	public void setBaseRateBean(BaseRate baseRateBean) {
		this.baseRateBean = baseRateBean;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
