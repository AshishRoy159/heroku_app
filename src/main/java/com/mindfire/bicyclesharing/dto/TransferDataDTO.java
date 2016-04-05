package com.mindfire.bicyclesharing.dto;

public class TransferDataDTO {

	private String vehicleNo;
	private Long transferId;

	/**
	 * @return the vehicleNo
	 */
	public String getVehicleNo() {
		return vehicleNo;
	}

	/**
	 * @param vehicleNo
	 *            the vehicleNo to set
	 */
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	/**
	 * @return the transferId
	 */
	public Long getTransferId() {
		return transferId;
	}

	/**
	 * @param transferId
	 *            the transferId to set
	 */
	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}
}
