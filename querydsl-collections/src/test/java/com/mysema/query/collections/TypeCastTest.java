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
package com.mysema.query.collections;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.types.path.PathInits;

import static org.junit.Assert.assertEquals;

public class TypeCastTest {

    @Test(expected=IllegalStateException.class)
    public void Cast() {
        QAnimal animal = QAnimal.animal;
        QCat cat = new QCat(animal.getMetadata(), new PathInits("*"));
        System.out.println(cat);
        MiniApi.from(animal, Collections.<Animal> emptyList()).from(cat, Collections.<Cat> emptyList());
    }

    @Test
    public void PropertyCast() {
        Post post = new Post(0, "", new User2("bla@bla.com"));

        assertEquals("bla@bla.com", MiniApi.from(QPost.post, post).singleResult(QPost.post.user.as(QUser2.class).email));
    }
}
