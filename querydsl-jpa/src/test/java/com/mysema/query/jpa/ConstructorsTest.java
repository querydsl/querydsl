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
package com.mysema.query.jpa;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;

public class ConstructorsTest extends AbstractQueryTest{

    public static final class BookmarkDTO {

    }

    public static final class _BookmarkDTO extends ConstructorExpression<BookmarkDTO> {

        private static final long serialVersionUID = 2664671413344744578L;

        public _BookmarkDTO(Expression<java.lang.String> address) {
            super(BookmarkDTO.class, new Class[] { String.class }, address);
        }
    }

    @Test
    @Ignore
    public void Constructors() {
        ConstructorExpression<com.mysema.query.jpa.domain.Cat> c =
                new ConstructorExpression<com.mysema.query.jpa.domain.Cat>(
                com.mysema.query.jpa.domain.Cat.class,
                new Class[]{String.class},
                cat.name);
        assertToString("new " + com.mysema.query.jpa.domain.Cat.class.getName()+ "(cat.name)", c);
        assertToString("new " + getClass().getName() + "$BookmarkDTO(cat.name)",new _BookmarkDTO(cat.name));
    }

}
