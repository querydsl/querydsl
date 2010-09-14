/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.ConstructorExpression;

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
    public void testConstructors() {
        ConstructorExpression<com.mysema.query.jpa.domain.Cat> c =
                new ConstructorExpression<com.mysema.query.jpa.domain.Cat>(
                com.mysema.query.jpa.domain.Cat.class,
                new Class[]{String.class},
                cat.name);
        assertToString("new " + com.mysema.query.jpa.domain.Cat.class.getName()+ "(cat.name)", c);
        assertToString("new " + getClass().getName() + "$BookmarkDTO(cat.name)",new _BookmarkDTO(cat.name));
    }

}
