/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.support;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.QueryException;
import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.function.CastFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.engine.SessionFactoryImplementor;

/**
 * @author tiwe
 *
 */
public class ExtendedDerbyDialect extends DerbyDialect{
    
    private static final CastFunction castFunction = new CastFunction(){
        @SuppressWarnings("unchecked")
        public String render(List args, SessionFactoryImplementor factory) throws QueryException {
            if (args.get(1).equals("string")){
                return super.render(Arrays.<Object>asList("char("+args.get(0)+")",args.get(1)), factory);    
            }else{
                return super.render(args, factory);
            }                
        }
    }; 
    
    public ExtendedDerbyDialect(){
        registerFunction( "concat", new VarArgsSQLFunction( Hibernate.STRING, "cast ((","||",") as varchar(128))" ) );
        registerFunction( "cast", castFunction );
    }

}
