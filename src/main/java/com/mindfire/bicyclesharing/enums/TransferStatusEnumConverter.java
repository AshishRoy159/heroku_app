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

package com.mindfire.bicyclesharing.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * TransferStatusEnumConverter class is used for converting transfer status enum
 * data for database.
 * 
 * @author mindfire
 * @version 1.0
 * @since 10/03/2016
 */
@Converter
public class TransferStatusEnumConverter implements AttributeConverter<TransferStatusEnum, Integer> {

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public Integer convertToDatabaseColumn(TransferStatusEnum attribute) {
		switch (attribute) {
		case PENDING:
			return new Integer(0);
		case IN_TRANSITION:
			return new Integer(1);
		case CLOSED:
			return new Integer(2);
		default:
			throw new IllegalArgumentException("Unknown" + attribute);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public TransferStatusEnum convertToEntityAttribute(Integer dbData) {
		if (dbData == 0) {
			return TransferStatusEnum.PENDING;
		} else if (dbData == 1) {
			return TransferStatusEnum.IN_TRANSITION;
		} else if (dbData == 2) {
			return TransferStatusEnum.CLOSED;
		} else {
			throw new IllegalArgumentException("Unknown" + dbData);
		}
	}
}
