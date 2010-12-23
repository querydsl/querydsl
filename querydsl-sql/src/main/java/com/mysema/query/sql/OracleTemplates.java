/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.math.BigInteger;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.sql.support.SerializationContext;
import com.mysema.query.types.Ops;

/**
 * OracleTemplates is an SQL dialect for Oracle
 *
 * tested with Oracle 10g XE
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleTemplates extends SQLTemplates {

    private final String outerQueryEnd = "\n ) a) where ";

    private final String outerQueryStart = "select * from (\n select a.*, rownum rn from (\n  ";

    private final String limitQueryEnd = "\n) where rownum <= {0}";

    private final String limitQueryStart = "select * from (\n  ";

    private final String limitOffsetTemplate = "rn > {0s} and rn <= {1s}";

    private final String offsetTemplate = "rn > {0}";

    public OracleTemplates(){
        this(false);
    }

    public OracleTemplates(boolean quote){
        super("\"", quote);
        // type mappings
        addClass2TypeMappings("number(3,0)", Byte.class);
        addClass2TypeMappings("number(1,0)", Boolean.class);
        addClass2TypeMappings("number(19,0)", BigInteger.class, Long.class);
        addClass2TypeMappings("number(5,0)", Short.class);
        addClass2TypeMappings("number(10,0)", Integer.class);
        addClass2TypeMappings("double precision", Double.class);
        addClass2TypeMappings("varchar(4000 char)", String.class);

        add(Ops.ALIAS, "{0} {1}");

        // String
        add(Ops.CONCAT, "{0} || {1}");
        add(Ops.INDEX_OF, "instrb({0},{1})-1");
        add(Ops.INDEX_OF_2ARGS, "instrb({0},{1},{2}+1)-1");
        add(Ops.MATCHES, "regexp_like({0},{1})");
        add(Ops.StringOps.SPACE, "lpad('',{0},' ')");
        
        // Number
        add(Ops.MathOps.CEIL, "ceil({0})");
        add(Ops.MathOps.RANDOM, "dbms_random.value");
        add(Ops.MathOps.LOG, "ln({0})");
        add(Ops.MathOps.LOG10, "log(10,{0})");

        // Date / time
        add(Ops.DateTimeOps.YEAR, "extract(year from {0})");
        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year from {0}) * 100 + extract(month from {0})");
        add(Ops.DateTimeOps.MONTH, "extract(month from {0})");
        add(Ops.DateTimeOps.WEEK, "to_number(to_char({0},'WW'))");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "to_number(to_char({0},'DD'))");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "to_number(to_char({0},'D')) + 1");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "to_number(to_char({0},'DDD'))");
        add(Ops.DateTimeOps.HOUR, "to_number(to_char({0},'HH24'))");
        add(Ops.DateTimeOps.MINUTE, "to_number(to_char({0},'MI'))");
        add(Ops.DateTimeOps.SECOND, "to_number(to_char({0},'SS'))");

    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SerializationContext context) {
        if (!forCountRow && metadata.getModifiers().isRestricting()){
            QueryModifiers mod = metadata.getModifiers();

            if (mod.getOffset() == null){
                context.append(limitQueryStart);
                context.serialize(metadata, forCountRow);
                context.handle(limitQueryEnd, mod.getLimit());
            }else{
                context.append(outerQueryStart);
                context.serialize(metadata, forCountRow);
                context.append(outerQueryEnd);

                if (mod.getLimit() == null){
                    context.handle(offsetTemplate, mod.getOffset());
                }else{
                    context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit() + mod.getOffset());
                }
            }

        }else{
            context.serialize(metadata, forCountRow);
        }
    }

}
