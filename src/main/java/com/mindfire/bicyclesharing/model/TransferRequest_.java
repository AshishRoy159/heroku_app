package com.mindfire.bicyclesharing.model;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-04-27T16:38:38.557+0530")
@StaticMetamodel(TransferRequest.class)
public class TransferRequest_ {
	public static volatile SingularAttribute<TransferRequest, Long> requestId;
	public static volatile SingularAttribute<TransferRequest, PickUpPoint> pickUpPoint;
	public static volatile SingularAttribute<TransferRequest, User> manager;
	public static volatile SingularAttribute<TransferRequest, Integer> quantity;
	public static volatile SingularAttribute<TransferRequest, Integer> approvedQuantity;
	public static volatile SingularAttribute<TransferRequest, Timestamp> requestedOn;
	public static volatile SingularAttribute<TransferRequest, Boolean> isApproved;
}
