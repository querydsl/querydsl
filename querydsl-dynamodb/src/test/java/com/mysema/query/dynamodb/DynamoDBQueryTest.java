package com.mysema.query.dynamodb;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.mysema.query.dynamodb.domain.QUser;
import com.mysema.query.dynamodb.domain.User;
import com.mysema.query.dynamodb.domain.User.Gender;
import com.mysema.query.types.Predicate;

public class DynamoDBQueryTest {

    private final QUser user = QUser.user;

    static User u1, u2, u3, u4;

    private static DynamoDBMapper mapper;

    private static AmazonDynamoDB client;

    @BeforeClass
    public static void setUp() throws Exception {
        client = ClientFactory.getInstance();
        mapper = new DynamoDBMapper(client, new DynamoDBMapperConfig(
                DynamoDBMapperConfig.SaveBehavior.CLOBBER,
                DynamoDBMapperConfig.ConsistentReads.CONSISTENT, null));

        fillTable();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        mapper.batchDelete(u1, u2, u3, u4);
        ClientFactory.shutdownInstance();
    }

    public static void fillTable() throws UnknownHostException {
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
        provisionedThroughput.setReadCapacityUnits(1L);
        provisionedThroughput.setWriteCapacityUnits(1L);

        if (!client.listTables().getTableNames().contains("User")) {
            CreateTableRequest createTableRequest = new CreateTableRequest()
                    .withTableName("User")
                    .withKeySchema(
                            new KeySchemaElement().withAttributeName("id")
                                    .withKeyType(KeyType.HASH))
                    .withProvisionedThroughput(provisionedThroughput)
                    .withAttributeDefinitions(
                            new AttributeDefinition().withAttributeName("id").withAttributeType(
                                    ScalarAttributeType.S));
            client.createTable(createTableRequest);
        } else {
            DynamoDBScanExpression scan = new DynamoDBScanExpression();
            PaginatedScanList<User> users = mapper.scan(User.class, scan);
            for (User user : users) {
                Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
                key.put("id", new AttributeValue().withS(user.getId()));
                client.deleteItem(new DeleteItemRequest("User", key));
            }
        }

        u1 = addUser("Jaakko", "Jantunen", 20, Gender.MALE, null);
        u2 = addUser("Jaakki", "Jantunen", 30, Gender.FEMALE, "One detail");
        u3 = addUser("Jaana", "Aakkonen", 40, Gender.MALE, "No details");
        u4 = addUser("Jaana", "BeekkoNen", 50, Gender.FEMALE, null);
    }

    private static User addUser(String first, String last, int age, Gender gender, String details) {
        User user = new User(first, last, age, new Date());
        user.setGender(gender);
        user.setDetails(details);
        mapper.save(user);
        System.out.println(user.getId());
        return user;
    }

    @Test
    public void eq() {
        User u = where(user.firstName.eq("Jaakko")).uniqueResult();
        assertThat(u, equalTo(u1));
    }

    @Test
    public void notEq() {
        if (!ClientFactory.isUsingDynamoMock())
            return;
        List<User> result = where(user.lastName.ne("Jantunen")).list();
        assertThat(result, containsInAnyOrder(u3, u4));
    }

    @Test
    public void beginsWith() {
        List<User> result = where(user.firstName.startsWith("Jaak")).list();
        assertThat(result, containsInAnyOrder(u1, u2));
    }

    @Test
    public void between() {
        List<User> result = where(user.age.between(29, 41)).list();
        assertThat(result, containsInAnyOrder(u3, u2));
    }

    @Test
    public void contains() {
        List<User> result = where(user.lastName.contains("ekko")).list();
        assertThat(result, containsInAnyOrder(u4));
    }

    @Test
    public void notContains() {
        if (!ClientFactory.isUsingDynamoMock())
            return;

        List<User> result = where(user.lastName.contains("nen").not()).list();
        assertThat(result, containsInAnyOrder(u4));
    }

    @Test
    public void greaterOrEquals() {
        List<User> result = where(user.age.goe(40)).list();
        assertThat(result, containsInAnyOrder(u3, u4));
    }

    @Test
    public void greaterThen() {
        List<User> result = where(user.age.gt(20)).list();
        assertThat(result, containsInAnyOrder(u2, u3, u4));
    }

    @Test
    public void lowerOrEquals() {
        List<User> result = where(user.age.loe(20)).list();
        assertThat(result, containsInAnyOrder(u1));
    }

    @Test
    public void lowerThen() {
        List<User> result = where(user.age.lt(40)).list();
        assertThat(result, containsInAnyOrder(u1, u2));
    }

    @Test
    public void in() {
        List<User> result = where(user.age.in(40)).list();
        assertThat(result, containsInAnyOrder(u3));
    }

    @Test
    public void isNull() {
        if (!ClientFactory.isUsingDynamoMock())
            return;

        List<User> result = where(user.details.isNull()).list();
        assertThat(result, containsInAnyOrder(u1, u4));
    }

    @Test
    public void isNotNull() {
        if (!ClientFactory.isUsingDynamoMock())
            return;

        List<User> result = where(user.details.isNotNull()).list();
        assertThat(result, containsInAnyOrder(u2, u3));
    }

    private DynamoDBQuery<User> query() {
        return new DynamoDBQuery<User>(client, user);
    }

    private DynamoDBQuery<User> where(Predicate... e) {
        return query().where(e);
    }

}
