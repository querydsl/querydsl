/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.sql;

import java.util.List;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;


/**
 * SQLServer2005Templates is an SQL dialect for Microsoft SQL Server 2005
 *
 * @author tiwe
 */
public class SQLServer2005Templates extends SQLServerTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final SQLServer2005Templates DEFAULT = new SQLServer2005Templates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new SQLServer2005Templates(escape, quote);
            }
        };
    }

    private String topTemplate = "top ({0}) ";

    private String outerQueryStart = "select * from (\n  ";

    private String outerQueryEnd = ") a where ";

    private String limitOffsetTemplate = "rn > {0} and rn <= {1}";

    private String offsetTemplate = "rn > {0}";

    private String outerQuerySuffix = " order by rn";

    public SQLServer2005Templates() {
        this('\\',false);
    }

    public SQLServer2005Templates(boolean quote) {
        this('\\',quote);
    }

    public SQLServer2005Templates(char escape, boolean quote) {
        super(escape, quote);
        //The older MSSQL Server suite doesn't support logarithms with a base other
        //than 10 and the natural logarithm, so we do it manually
        add(Ops.MathOps.LOG, "(LOG({0}) / LOG({1}))");
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        if (!forCountRow && metadata.getModifiers().isRestricting() && !metadata.getJoins().isEmpty()) {
            QueryModifiers mod = metadata.getModifiers();
            if (mod.getOffset() == null) {
                // select top ...
                metadata = metadata.clone();
                metadata.addFlag(new QueryFlag(QueryFlag.Position.AFTER_SELECT,
                        Expressions.template(Integer.class, topTemplate, mod.getLimit())));
                context.serializeForQuery(metadata, forCountRow);
            } else {
                context.append(outerQueryStart);
                metadata = metadata.clone();
                WindowFunction<Long> rn = SQLExpressions.rowNumber().over();
                for (OrderSpecifier<?> os : metadata.getOrderBy()) {
                    rn.orderBy(os);
                }
                metadata.addProjection(rn.as("rn"));
                metadata.clearOrderBy();
                context.serializeForQuery(metadata, forCountRow);
                context.append(outerQueryEnd);
                if (mod.getLimit() == null) {
                    context.handle(offsetTemplate, mod.getOffset());
                } else {
                    context.handle(limitOffsetTemplate, mod.getOffset(), mod.getLimit() + mod.getOffset());
                }
                context.append(outerQuerySuffix);
            }

        } else {
            context.serializeForQuery(metadata, forCountRow);
        }

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    @Override
    public void serializeDelete(QueryMetadata metadata, RelationalPath<?> entity, SQLSerializer context) {
        // limit
        QueryModifiers mod = metadata.getModifiers();
        if (mod.isRestricting()) {
            metadata = metadata.clone();
            metadata.addFlag(new QueryFlag(QueryFlag.Position.AFTER_SELECT,
                    Expressions.template(Integer.class, topTemplate, mod.getLimit())));
        }

        context.serializeForDelete(metadata, entity);

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

    @Override
    public void serializeUpdate(QueryMetadata metadata, RelationalPath<?> entity,
            List<Pair<Path<?>, Expression<?>>> updates, SQLSerializer context) {
        // limit
        QueryModifiers mod = metadata.getModifiers();
        if (mod.isRestricting()) {
            metadata = metadata.clone();
            metadata.addFlag(new QueryFlag(QueryFlag.Position.AFTER_SELECT,
                    Expressions.template(Integer.class, topTemplate, mod.getLimit())));
        }

        context.serializeForUpdate(metadata, entity, updates);

        if (!metadata.getFlags().isEmpty()) {
            context.serialize(Position.END, metadata.getFlags());
        }
    }

}
