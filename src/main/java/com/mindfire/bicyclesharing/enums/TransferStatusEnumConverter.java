package com.mindfire.bicyclesharing.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TransferStatusEnumConverter implements AttributeConverter<TransferStatusEnum, Integer> {
	
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