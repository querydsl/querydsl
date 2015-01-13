/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.apt.domain.QDelegateTest_SimpleUser;
import com.querydsl.apt.domain.QDelegateTest_SimpleUser2;
import com.querydsl.apt.domain.QDelegateTest_User;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.path.StringPath;

public class DelegateTest {

    @QuerySupertype
    public static class Identifiable {

        long id;

    }

    @QueryEntity
    public static class User extends Identifiable {

        String name;

        User managedBy;

    }

    @QueryEntity
    public static class SimpleUser extends User {

    }

    @QueryEntity
    public static class SimpleUser2 extends SimpleUser {

    }

    @QueryDelegate(User.class)
    public static Expression<Boolean> isManagedBy(QDelegateTest_User user, User other) {
        return ConstantImpl.create(true);
    }

    @QueryDelegate(User.class)
    public static Expression<Boolean> isManagedBy(QDelegateTest_User user, QDelegateTest_User other) {
        return ConstantImpl.create(true);
    }

    @QueryDelegate(User.class)
    public static Expression<Boolean> simpleMethod(QDelegateTest_User user) {
        return ConstantImpl.create(true);
    }

    @QueryDelegate(DelegateTest.User.class)
    public static StringPath getName(QDelegateTest_User user) {
        return user.name;
    }

    @Test
    public void User() {
        QDelegateTest_User user = QDelegateTest_User.user;
        assertNotNull(user.isManagedBy(new User()));
        assertNotNull(user.isManagedBy(user));
        assertNotNull(user.simpleMethod());
        assertEquals(user.name, user.getName());
    }

    @Test
    public void SimpleUser() {
        QDelegateTest_SimpleUser user = QDelegateTest_SimpleUser.simpleUser;
        assertNotNull(user.isManagedBy(new User()));
        assertNotNull(user.isManagedBy(user._super));
        assertEquals(user.name, user.getName());
    }

    @Test
    public void SimpleUser2() {
        QDelegateTest_SimpleUser2 user = QDelegateTest_SimpleUser2.simpleUser2;
        assertNotNull(user.isManagedBy(new User()));
        assertNotNull(user.isManagedBy(user._super._super));
        assertEquals(user.name, user.getName());
    }

}
