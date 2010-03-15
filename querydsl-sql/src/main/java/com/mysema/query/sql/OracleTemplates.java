/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.math.BigInteger;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.operation.Ops;

/**
 * OracleTemplates is an SQL dialect for Oracle
 * 
 * tested with Oracle 10g XE
 * 
 * @author tiwe
 * @version $Id$
 */
public class OracleTemplates extends SQLTemplates {
    
    private String limitOffsetTemplate = "rn > {0s} and rn <= {1s}";
    
    private String limitTemplate = "rn <= {0}";
    
    private String offsetTemplate = "rn > {0}";
    
    public OracleTemplates(){
        // type mappings
        addClass2TypeMappings("number(3,0)", Byte.class);
        addClass2TypeMappings("number(1,0)", Boolean.class);
        addClass2TypeMappings("number(19,0)", BigInteger.class, Long.class);
        addClass2TypeMappings("number(5,0)", Short.class);
        addClass2TypeMappings("number(10,0)", Integer.class);
        addClass2TypeMappings("double precision", Double.class);
        addClass2TypeMappings("varchar(4000 char)", String.class);

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
        
//        setLimitAndOffsetSymbols(false);
//        setRequiresWhereForPagingSymbols(true);
//        setLimitTemplate("rownum < {0}");
//        setOffsetTemplate("rownum > {0}");
//        setLimitOffsetTemplate("rownum between {0} and {2}");
    }
    
    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SerializationContext context) {
        if (!forCountRow && metadata.getModifiers().isRestricting()){
            QueryModifiers mod = metadata.getModifiers();
            
            // TODO : move template strings to instance fields
            if (mod.getOffset() == null){
                context.append("select * from (\n  ");
                context.serialize(metadata, forCountRow);
                context.handle("\n) where rownum <= {0}", mod.getLimit());            
            }else{
                context.append("select * from (\n");
                context.append(" select a.*, rownum rn from (\n  ");
                context.serialize(metadata, forCountRow);
                context.append("\n ) a) where ");            
                
                if (mod.getLimit() == null){
                    context.handle(offsetTemplate, mod.getOffset());
                }else if (mod.getOffset() == null){
                    context.handle(limitTemplate, mod.getLimit());
                }else{
                    context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit() + mod.getOffset());
                }
            }
              
        }else{
            context.serialize(metadata, forCountRow);    
        }        
    }
    
}