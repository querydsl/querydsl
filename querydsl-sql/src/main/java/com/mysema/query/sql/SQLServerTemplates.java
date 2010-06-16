/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.sql.mssql.RowNumber;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.PNumber;

/**
 * SQLServerTemplates is an SQL dialect for Microsoft SQL Server
 * 
 * tested with MS SQL Server 2008 Express
 * 
 * @author tiwe
 *
 */
public class SQLServerTemplates extends SQLTemplates{
    
    private static final PNumber<Long> rowNumber = new PNumber<Long>(Long.class, "row_number");
    
    private String limitOffsetTemplate = "row_number > {0} and row_number <= {1}";
    
    private String limitTemplate = "row_number <= {0}";
    
    private String offsetTemplate = "row_number > {0}";
    
    private String outerQueryStart = "with inner_query as \n(\n  ";
    
    private String outerQueryEnd = "\n)\nselect * \nfrom inner_query\nwhere ";
    
    public SQLServerTemplates(){
        this(false);
    }
    
    public SQLServerTemplates(boolean quote){
        super(quote ? "\"" : null);
        addClass2TypeMappings("decimal", Double.class);
        
        // String        
        add(Ops.CHAR_AT, "cast(substring({0},{1}+1,1) as char)");
        add(Ops.INDEX_OF, "charindex({1},{0})-1");
        add(Ops.INDEX_OF_2ARGS, "charindex({1},{0},{2})-1");
        // NOTE : needs to be replaced with real regular expression
        add(Ops.MATCHES, "{0} like {1}"); 
        add(Ops.STRING_IS_EMPTY, "len({0}) = 0");
        add(Ops.STRING_LENGTH, "len({0})");
        add(Ops.SUBSTR_1ARG, "substring({0},{1}+1,255)");
        add(Ops.SUBSTR_2ARGS, "substring({0},{1}+1,{2})");
        add(Ops.TRIM, "ltrim(rtrim({0}))");
        
        // Date / time
        add(Ops.DateTimeOps.YEAR, "datepart(year, {0})");
        add(Ops.DateTimeOps.MONTH, "datepart(month, {0})");
        add(Ops.DateTimeOps.WEEK, "datepart(week, {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "datepart(day, {0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "datepart(weekday, {0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "datepart(dayofyear, {0})");
        add(Ops.DateTimeOps.HOUR, "datepart(hour, {0})");
        add(Ops.DateTimeOps.MINUTE, "datepart(minute, {0})");
        add(Ops.DateTimeOps.SECOND, "datepart(second, {0})");
        add(Ops.DateTimeOps.MILLISECOND, "datepart(millisecond, {0})");
                
    }
    
    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SerializationContext context) {
        if (!forCountRow && metadata.getModifiers().isRestricting()){
            // TODO : provide simpler template for limit ?!?
            
            context.append(outerQueryStart);            
            metadata = metadata.clone();
            RowNumber rn = new RowNumber();
            for (OrderSpecifier<?> os : metadata.getOrderBy()){
                rn.orderBy(os);
            }            
            metadata.addProjection(rn.as(rowNumber));
            metadata.clearOrderBy();
            context.serialize(metadata, forCountRow);            
            context.append(outerQueryEnd);            
            QueryModifiers mod = metadata.getModifiers();
            if (mod.getLimit() == null){
                context.handle(offsetTemplate, mod.getOffset());
            }else if (mod.getOffset() == null){
                context.handle(limitTemplate, mod.getLimit());
            }else{
                context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit() + mod.getOffset());
            }
            
        }else{
            context.serialize(metadata, forCountRow);
        }
    }
}
