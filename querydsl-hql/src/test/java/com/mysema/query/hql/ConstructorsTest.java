package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;

public class ConstructorsTest extends AbstractQueryTest{

    public static final class BookmarkDTO {

    }
    
    public static final class _BookmarkDTO extends EConstructor<BookmarkDTO> {

        private static final long serialVersionUID = 2664671413344744578L;

        public _BookmarkDTO(Expr<java.lang.String> address) {
            super(BookmarkDTO.class, new Class[] { String.class }, address);
        }
    }
    
    @Test
    public void testConstructors() {
        EConstructor<com.mysema.query.hql.domain.Cat> c = 
                new EConstructor<com.mysema.query.hql.domain.Cat>(
                com.mysema.query.hql.domain.Cat.class,
                new Class[]{String.class},
                cat.name);
        assertToString("new " + com.mysema.query.hql.domain.Cat.class.getName()+ "(cat.name)", c);
        assertToString("new " + getClass().getName() + "$BookmarkDTO(cat.name)",new _BookmarkDTO(cat.name));
    }


}
