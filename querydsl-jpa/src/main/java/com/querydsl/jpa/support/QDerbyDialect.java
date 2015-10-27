/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.jpa.support;

import java.util.Arrays;
import java.util.List;

import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.function.CastFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

import com.querydsl.core.types.Ops;
import com.querydsl.sql.DerbyTemplates;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code QDerbyDialect} extends {@code DerbyDialect} with additional functions
 */
public class QDerbyDialect extends DerbyDialect {

    private static final CastFunction castFunction = new CastFunction() {
        @Override
        public String render(Type columnType, List args, SessionFactoryImplementor factory) {
            if (args.get(1).equals("string")) {
                return super.render(columnType, Arrays.asList("char(" + args.get(0) + ")", args.get(1)), factory);
            } else {
                return super.render(columnType, args, factory);
            }
        }
    };

    public QDerbyDialect() {
        SQLTemplates templates = DerbyTemplates.DEFAULT;
        getFunctions().putAll(DialectSupport.createFunctions(templates));
        registerFunction("concat", DialectSupport.createFunction(templates, Ops.CONCAT));
        registerFunction("cast", castFunction);
    }

}
