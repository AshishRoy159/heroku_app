package com.mindfire.bicyclesharing.dto;

public class ManageRateGroupDTO {

	private Long userId;
	private int rateGroupId;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the rateGroupId
	 */
	public int getRateGroupId() {
		return rateGroupId;
	}

	/**
	 * @param rateGroupId
	 *            the rateGroupId to set
	 */
	public void setRateGroupId(int rateGroupId) {
		this.rateGroupId = rateGroupId;
	}
}
