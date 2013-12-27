package com.mysema.query.dynamodb;

import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.michelboudreau.alternator.AlternatorDB;
import com.michelboudreau.alternatorv2.AlternatorDBClientV2;

public class ClientFactory {

    private static AmazonDynamoDB instance;
    private static AlternatorDB db;

    public static AmazonDynamoDB getInstance() throws Exception {
        if (instance != null)
            return instance;

        if (isUsingDynamoMock()) {
            instance = new AmazonDynamoDBClient(new SystemPropertiesCredentialsProvider());
            instance.setRegion(Region.getRegion(Regions.US_EAST_1));
        } else {
            instance = new AlternatorDBClientV2();
            db = new AlternatorDB();
            db.start();
        }
        return instance;
    }

    public static boolean isUsingDynamoMock() {
        return System.getProperties().containsKey("aws.secretKey");
    }

    public static void shutdownInstance() throws Exception {
        if (instance == null)
            return;

        instance.shutdown();
        if (isUsingDynamoMock()) {
        } else {
            db.stop();
        }
        instance = null;
        db = null;
    }

}
