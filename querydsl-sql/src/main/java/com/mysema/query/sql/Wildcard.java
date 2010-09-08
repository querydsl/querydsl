package com.mysema.query.sql;

import com.mysema.query.types.Expr;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.ENumber;

public class Wildcard {

    private static final long serialVersionUID = -675749944676437551L;

    public static final Expr<Object[]> all = CSimple.create(Object[].class, "*");

    public static ENumber<Long> count() {
        return all.count();
    }

}
