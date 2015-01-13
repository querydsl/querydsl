package com.querydsl.jpa.support;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

/**
 * @author tiwe
 *
 */
public class ExtendedOracleDialect extends Oracle10gDialect {
    
    public ExtendedOracleDialect() {
        // there fail otherwise with the time datatype
        registerFunction( "second", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "to_number(to_char(?1,'SS'))") );
        registerFunction( "minute", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "to_number(to_char(?1,'MI'))") );
        registerFunction( "hour", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "to_number(to_char(?1,'HH24'))") );
    }

}
