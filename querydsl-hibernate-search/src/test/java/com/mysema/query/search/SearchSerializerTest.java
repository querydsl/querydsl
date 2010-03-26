package com.mysema.query.search;

import static org.junit.Assert.*;

import org.junit.Test;

public class SearchSerializerTest {

    @Test
    public void testToField() {
        SearchSerializer serializer = new SearchSerializer(true);
        QUser user = new QUser("user");
        assertEquals("email", serializer.toField(user.emailAddress));
        assertEquals("firstName", serializer.toField(user.firstName));
    }

}
