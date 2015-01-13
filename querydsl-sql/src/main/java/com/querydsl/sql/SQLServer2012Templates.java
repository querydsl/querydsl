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

import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.support.Expressions;

/**
 * SQLServer2012Templates is an SQL dialect for Microsoft SQL Server 2012 and later
 *
 * @author tiwe
 *
 */
public class SQLServer2012Templates extends SQLServerTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final SQLServer2012Templates DEFAULT = new SQLServer2012Templates();

    private String topTemplate = "top {0s} ";

    private String limitOffsetTemplate = "\noffset {1} rows fetch next {0} rows only";

    private String offsetTemplate = "\noffset {0} rows";

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new SQLServer2012Templates(escape, quote);
            }
        };
    }

    public SQLServer2012Templates() {
        this('\\',false);
    }

    public SQLServer2012Templates(boolean quote) {
        this('\\',quote);
    }

    public SQLServer2012Templates(char escape, boolean quote) {
        super(escape, quote);
    }

    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow, SQLSerializer context) {
        if (!forCountRow && metadata.getModifiers().isRestricting() && metadata.getOrderBy().isEmpty()
                && !metadata.getJoins().isEmpty()) {
            QueryModifiers mod = metadata.getModifiers();
            // use top if order by is empty
            if (mod.getOffset() == null) {
                // select top ...
                metadata = metadata.clone();
                metadata.addFlag(new QueryFlag(QueryFlag.Position.AFTER_SELECT,
                        Expressions.template(Integer.class, topTemplate, mod.getLimit())));
                context.serializeForQuery(metadata, forCountRow);
            } else {
                throw new IllegalStateException("offset not supported without order by");
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
        if (!metadata.getOrderBy().isEmpty()) {
            QueryModifiers mod = metadata.getModifiers();
            if (mod.getLimit() == null) {
                context.handle(offsetTemplate, mod.getOffset());
            } else if (mod.getOffset() == null) {
                context.handle(limitOffsetTemplate, mod.getLimit(), 0);
            } else {
                context.handle(limitOffsetTemplate, mod.getLimit(), mod.getOffset());
            }
        }
    }

}
