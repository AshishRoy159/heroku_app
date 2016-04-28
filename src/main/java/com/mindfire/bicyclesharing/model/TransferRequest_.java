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

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The (auto generated) meta model class for the {@link TransferRequest} entity.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Generated(value = "Dali", date = "2016-04-27T16:38:38.557+0530")
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
