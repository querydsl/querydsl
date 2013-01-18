/*
 * Copyright 2011, Mysema Ltd
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
package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.sql.mssql.RowNumber;
import com.mysema.query.sql.mssql.SQLServerGrammar;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;

/**
 * SQLServerTemplates is an SQL dialect for Microsoft SQL Server
 *
 * <p>tested with MS SQL Server 2008 Express</p>
 *
 * @author tiwe
 *
 */
public class SQLServerTemplates extends SQLTemplates{

    private String limitOffsetTemplate = "row_number > {0} and row_number <= {1}";

    private String limitTemplate = "row_number <= {0}";

    private String offsetTemplate = "row_number > {0}";

    private String outerQueryStart = "with inner_query as \n(\n  ";

    private String outerQueryEnd = "\n)\nselect * \nfrom inner_query\nwhere ";

    public SQLServerTemplates() {
        this('\\',false);
    }
    
    public SQLServerTemplates(boolean quote) {
        this('\\',quote);
    }

    public SQLServerTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        addClass2TypeMappings("decimal", Double.class);
        setDummyTable("");

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

        add(NEXTVAL, "{0s}.nextval");
        
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
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        if (!forCountRow && metadata.getModifiers().isRestricting() && !metadata.getJoins().isEmpty()) {
            // TODO : provide simpler template for limit ?!?

            context.append(outerQueryStart);
            metadata = metadata.clone();
            RowNumber rn = new RowNumber();
            for (OrderSpecifier<?> os : metadata.getOrderBy()) {
                rn.orderBy(os);
            }
            metadata.addProjection(rn.as(SQLServerGrammar.rowNumber));
            metadata.clearOrderBy();
            context.serializeForQuery(metadata, forCountRow);
            context.append(outerQueryEnd);
            QueryModifiers mod = metadata.getModifiers();
            if (mod.getLimit() == null) {
                context.handle(offsetTemplate, mod.getOffset());
            } else if (mod.getOffset() == null) {
                context.handle(limitTemplate, mod.getLimit());
            } else {
                context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit() + mod.getOffset());
            }

        } else {
            context.serializeForQuery(metadata, forCountRow);
        }
        
        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());    
        }   
    }
    
    @Override
    protected void serializeModifiers(QueryMetadata metadata, SQLSerializer context) {
        // do nothing
    }

    
}
