package com.mysema.query.dynamodb.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;
import com.mysema.query.dynamodb.domain.User.Gender;

public class GenderConverter implements DynamoDBMarshaller<Gender> {

    @Override
    public String marshall(Gender gender) {
        if (gender == null) {
            return null;
        }

        return gender.name();
    }

    @Override
    public Gender unmarshall(Class<Gender> clazz, String gender) {
        if (gender == null) {
            return null;
        }

        return Enum.valueOf(clazz, gender);
    }
}