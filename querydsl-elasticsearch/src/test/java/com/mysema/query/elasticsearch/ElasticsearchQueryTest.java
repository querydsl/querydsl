package com.mysema.query.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.SearchResults;
import com.mysema.query.elasticsearch.domain.QUser;
import com.mysema.query.elasticsearch.domain.User;
import com.mysema.query.elasticsearch.jackson.JacksonElasticsearchQueries;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.testutil.ExternalDB;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.elasticsearch.client.Requests.refreshRequest;
import static org.junit.Assert.*;

@Category(ExternalDB.class)
public class ElasticsearchQueryTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static Client client;

    private final String indexUser = "index1";
    private final String typeUser = "user";

    private final QUser user = QUser.user;
    List<User> users = Lists.newArrayList();
    User u1, u2, u3, u4;

    public ElasticsearchQueryTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        ImmutableSettings.Builder settings = ImmutableSettings.builder().put("path.data", ElasticsearchQueryTest.class.getResource("").getPath());
        Node node = NodeBuilder.nodeBuilder().local(true).settings(settings).node();
        client = node.client();

    }

    @Before
    public void before() {
        deleteType(indexUser);
        createIndex(indexUser);

        u1 = addUser("Jaakko", "Jantunen", 20);
        u2 = addUser("Jaakki", "Jantunen", 30);
        u3 = addUser("Jaana", "Aakkonen", 40);
        u4 = addUser("Jaana", "BeekkoNen", 50);

        refresh(indexUser, false);
    }

    @Test
    public void Count() {
        assertEquals(4, query().count());
    }

    @Test
    public void Count_Predicate() {
        assertEquals(2, where(user.lastName.eq("Jantunen")).count());
    }

    @Test
    public void SingleResult_Keys() {
        User u = where(user.firstName.eq("Jaakko")).singleResult(user.firstName);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
        assertEquals(0, u.getAge());
    }

    @Test
    public void UniqueResult_Keys() {
        User u = where(user.firstName.eq("Jaakko")).uniqueResult(user.firstName);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
        assertEquals(0, u.getAge());
    }

    @Test(expected = NonUniqueResultException.class)
    public void UniqueResult_Keys_Non_Unique() {
        where(user.firstName.eq("Jaana")).uniqueResult(user.firstName);
    }

    @Test
    public void Contains() {
        //assertQuery(user.friends.contains(u1), u3, u4, u2);
    }

    @Test
    public void Contains2() {
        //assertQuery(user.friends.contains(u4));
    }

    @Test
    public void NotContains() {
        //assertQuery(user.friends.contains(u1).not(), u1);
    }

    @Test
    public void Equals_Ignore_Case() {
        assertTrue(where(user.firstName.equalsIgnoreCase("jAaKko")).exists());
        assertFalse(where(user.firstName.equalsIgnoreCase("AaKk")).exists());
    }

    @Test
    public void Starts_With_and_Between() {
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.between(20, 30)), u2, u1);
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.goe(20).and(user.age.loe(30))), u2, u1);
    }

    @Test
    public void Exists() {
        assertTrue(where(user.firstName.eq("Jaakko")).exists());
        assertFalse(where(user.firstName.eq("JaakkoX")).exists());
        assertTrue(where(user.id.eq(u1.getId())).exists());
    }

    @Test
    public void Find_By_Id() {
        assertNotNull(where(user.id.eq(u1.getId())).uniqueResult());
    }

    @Test
    public void Find_By_Ids() {
        assertQuery(user.id.in(u1.getId(), u2.getId()), u2, u1);
    }

    @Test
    public void Order() {
        List<User> users = query().orderBy(user.age.asc()).list();
        assertEquals(asList(u1, u2, u3, u4), users);

        users = query().orderBy(user.age.desc()).list();
        assertEquals(asList(u4, u3, u2, u1), users);
    }

    @Test
    public void ListResults() {
        SearchResults<User> results = query().limit(2).orderBy(user.age.asc()).listResults();
        assertEquals(4l, results.getTotal());
        assertEquals(2, results.getResults().size());

        results = query().offset(2).orderBy(user.age.asc()).listResults();
        assertEquals(4l, results.getTotal());
        assertEquals(2, results.getResults().size());
    }

    @Test
    public void EmptyResults() {
        SearchResults<User> results = query().where(user.firstName.eq("XXX")).listResults();
        assertEquals(0l, results.getTotal());
        assertEquals(Collections.<User>emptyList(), results.getResults());
    }

    @Test
    public void EqInAndOrderByQueries() {
        assertQuery(user.firstName.eq("Jaakko"), u1);
        assertQuery(user.firstName.equalsIgnoreCase("jaakko"), u1);
        assertQuery(user.lastName.eq("Aakkonen"), u3);

        assertQuery(user.firstName.in("Jaakko","Teppo"), u1);
        assertQuery(user.lastName.in("Aakkonen", "BeekkoNen"), u3, u4);

        assertQuery(user.firstName.eq("Jouko"));

        assertQuery(user.firstName.eq("Jaana"), user.lastName.asc(), u3, u4);
        assertQuery(user.firstName.eq("Jaana"), user.lastName.desc(), u4, u3);
        assertQuery(user.lastName.eq("Jantunen"), user.firstName.asc(), u2, u1);
        assertQuery(user.lastName.eq("Jantunen"), user.firstName.desc(), u1, u2);

        assertQuery(user.firstName.eq("Jaana").and(user.lastName.eq("Aakkonen")), u3);
        //This shoud produce 'and' also
        assertQuery(where(user.firstName.eq("Jaana"), user.lastName.eq("Aakkonen")), u3);

        assertQuery(user.firstName.ne("Jaana"), u2, u1);
        assertQuery(user.firstName.ne("Jaana").and(user.lastName.ne("Jantunen")));
        assertQuery(user.firstName.eq("Jaana").and(user.lastName.eq("Aakkonen")).not(), u4, u2, u1);

    }

    @Test
    public void Iterate() {
        User a = addUser("A", "A", 10);
        User b = addUser("A1", "B", 10);
        User c = addUser("A2", "C", 10);

        refresh(indexUser, false);

        Iterator<User> i = where(user.firstName.startsWith("A"))
                .orderBy(user.firstName.asc())
                .iterate();

        assertEquals(a, i.next());
        assertEquals(b, i.next());
        assertEquals(c, i.next());
        assertEquals(false, i.hasNext());
    }

    @Test
    public void Enum_Eq() {
        assertQuery(user.gender.eq(User.Gender.MALE), u3, u4, u2, u1);
    }

    @Test
    public void Enum_Ne() {
        assertQuery(user.gender.ne(User.Gender.MALE));
    }

    private ElasticsearchQuery<User> query() {
        return new JacksonElasticsearchQueries(client).query(QUser.user, indexUser, typeUser);
    }

    private ElasticsearchQuery<User> where(Predicate predicate) {
        return query().where(predicate);
    }

    private ElasticsearchQuery<User> where(Predicate ... e) {
        return query().where(e);
    }

    private void assertQuery(Predicate e, User ... expected) {
        assertQuery(where(e).orderBy(user.lastName.asc(), user.firstName.asc()), expected);
    }

    private void assertQuery(Predicate e, OrderSpecifier<?> orderBy, User ... expected ) {
        assertQuery(where(e).orderBy(orderBy), expected);
    }

    private void assertQuery(ElasticsearchQuery<User> query, User ... expected ) {
        List<User> results = query.list();

        assertNotNull(results);
        if (expected == null ) {
            assertEquals("Should get empty result", 0, results.size());
            return;
        }
        assertEquals(expected.length, results.size());
        int i = 0;
        for (User u : expected) {
            assertEquals(u, results.get(i++));
        }
    }

    private User addUser(String first, String last, int age) {
        User user = new User(first, last, age, new Date());
        user.setGender(User.Gender.MALE);
        try {
            IndexResponse response = client.prepareIndex(indexUser, typeUser).setSource(mapper.writeValueAsString(user)).execute().actionGet();
            user.setId(response.getId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        users.add(user);
        return user;
    }

    public void deleteType(String index) {
        if (indexExists(index)) {
            client.admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
        }
    }

    public boolean indexExists(String index) {
        return client.admin().indices().exists(Requests.indicesExistsRequest(index)).actionGet().isExists();
    }

    public boolean createIndex(String index) {
        if (indexExists(index)) {
            return true;
        }

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(index);
        return createIndexRequestBuilder.execute().actionGet().isAcknowledged();
    }

    public void refresh(String indexName, boolean waitForOperation) {
        client.admin().indices().refresh(refreshRequest(indexName).force(waitForOperation)).actionGet();
    }

}
