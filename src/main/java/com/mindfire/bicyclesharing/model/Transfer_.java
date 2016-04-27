package com.mindfire.bicyclesharing.model;

import com.mindfire.bicyclesharing.enums.TransferStatusEnum;
import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-04-27T16:38:38.557+0530")
@StaticMetamodel(Transfer.class)
public class Transfer_ {
	public static volatile SingularAttribute<Transfer, Long> transferId;
	public static volatile SingularAttribute<Transfer, Timestamp> arrivedOn;
	public static volatile SingularAttribute<Transfer, Timestamp> dispatchedAt;
	public static volatile SingularAttribute<Transfer, Integer> quantity;
	public static volatile SingularAttribute<Transfer, PickUpPoint> transferredFrom;
	public static volatile SingularAttribute<Transfer, PickUpPoint> transferredTo;
	public static volatile SingularAttribute<Transfer, String> vehicleNo;
	public static volatile SingularAttribute<Transfer, TransferStatusEnum> status;
}
