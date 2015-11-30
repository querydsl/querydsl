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

import com.querydsl.core.types.EntityPath;

public class CollectionTest {

    @Test
    public void collectionUsage() {
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals("any(domainType.collection) = domainType", $(domainType.getCollection()).any().eq(domainType).toString());
        assertEquals("any(domainType.set) = domainType", $(domainType.getSet()).any().eq(domainType).toString());
        assertEquals("any(domainType.list) = domainType", $(domainType.getList()).any().eq(domainType).toString());
        assertEquals("domainType.list.get(0) = domainType", $(domainType.getList().get(0)).eq(domainType).toString());
        assertEquals("domainType.list.get(0) = domainType", $(domainType.getList()).get(0).eq(domainType).toString());
        assertEquals("domainType.map.get(key) = domainType", $(domainType.getMap()).get("key").eq(domainType).toString());

        EntityPath<DomainType> domainTypePath = $(domainType);
        assertEquals("domainType in domainType.collection", $(domainType.getCollection()).contains(domainTypePath).toString());
    }

    @Test
    public void collectionUsage_types() {
        DomainType domainType = Alias.alias(DomainType.class);
        assertEquals(DomainType.class, $(domainType.getCollection()).any().getType());
        assertEquals(DomainType.class, $(domainType.getSet()).any().getType());
        assertEquals(DomainType.class, $(domainType.getList()).any().getType());
        assertEquals(DomainType.class, $(domainType.getMap()).get("key").getType());
    }


}
