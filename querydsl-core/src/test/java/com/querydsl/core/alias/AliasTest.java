/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.alias;

import static org.junit.Assert.assertEquals;
import static com.querydsl.core.alias.Alias.$;

import org.junit.Test;

import com.querydsl.core.types.Path;

public class AliasTest {

    @Test
    public void alias() {
        DomainType domainType = Alias.alias(DomainType.class);
        Alias.alias(DomainType.class, $(domainType.getCollection()).any());
    }

    @Test
    public void comparableEntity() {
        ComparableEntity entity = Alias.alias(ComparableEntity.class);
        Path<ComparableEntity> path = $(entity);
        assertEquals(ComparableEntity.class, path.getType());
    }

    @Test
    public void comparableEntity_property() {
        ComparableEntity entity = Alias.alias(ComparableEntity.class);
        Path<String> propertyPath = $(entity.getProperty());
        assertEquals(String.class, propertyPath.getType());
        assertEquals("property", propertyPath.getMetadata().getName());
    }

    @Test
    public void basicUsage() {
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals("lower(domainType.firstName)", $(domainType.getFirstName()).lower().toString());
        assertEquals("domainType.age", $(domainType.getAge()).toString());
        assertEquals("domainType.map.get(a)", $(domainType.getMap().get("a")).toString());
        assertEquals("domainType.list.get(0)", $(domainType.getList().get(0)).toString());

        assertEquals("domainType.bigDecimal", $(domainType.getBigDecimal()).toString());
        assertEquals("domainType.bigInteger", $(domainType.getBigInteger()).toString());
        assertEquals("domainType.byte", $(domainType.getByte()).toString());
        assertEquals("domainType.collection", $(domainType.getCollection()).toString());
        assertEquals("domainType.double", $(domainType.getDouble()).toString());
        assertEquals("domainType.float", $(domainType.getFloat()).toString());
        assertEquals("domainType.date", $(domainType.getDate()).toString());
        assertEquals("domainType.date2", $(domainType.getDate2()).toString());
        assertEquals("domainType.set", $(domainType.getSet()).toString());
        assertEquals("domainType.short", $(domainType.getShort()).toString());
        assertEquals("domainType.time", $(domainType.getTime()).toString());
        assertEquals("domainType.timestamp", $(domainType.getTimestamp()).toString());
        assertEquals("domainType.gender", $(domainType.getGender()).toString());
    }

    @Test
    public void getAny() {
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals(DomainType.class, Alias.getAny(domainType).getType());
        assertEquals(String.class, Alias.getAny(domainType.getFirstName()).getType());
    }

    @Test
    public void otherMethods() {
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals("domainType", domainType.toString());
    }

    @Test
    public void var() {
        assertEquals("it", Alias.var().toString());
        assertEquals("varInteger1", Alias.var(1).toString());
        assertEquals("X", Alias.var("X").toString());
        assertEquals("varMALE", Alias.var(Gender.MALE).toString());
        assertEquals("varAliasTest_XXX", Alias.var(new AliasTest()).toString());
    }

    @Override
    public String toString() {
        return "XXX";
    }
}
