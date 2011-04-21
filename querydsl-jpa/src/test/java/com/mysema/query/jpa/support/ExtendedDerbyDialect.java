/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.support;

import java.util.Arrays;
import java.util.List;

import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.function.CastFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * @author tiwe
 *
 */
public class ExtendedDerbyDialect extends DerbyDialect{

    private static final CastFunction castFunction = new CastFunction(){
        @Override
        @SuppressWarnings("unchecked")
        public String render(Type columnType, List args, SessionFactoryImplementor factory) {
            if (args.get(1).equals("string")){
                return super.render(columnType, Arrays.<Object>asList("char("+args.get(0)+")",args.get(1)), factory);
            }else{
                return super.render(columnType, args, factory);
            }
        }
    };

    public ExtendedDerbyDialect(){
        registerFunction( "concat", new VarArgsSQLFunction( StandardBasicTypes.STRING, "cast ((","||",") as varchar(128))" ) );
        registerFunction( "cast", castFunction );
    }

}
