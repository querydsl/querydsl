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
package com.querydsl.r2dbc;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalFunctionCall;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class R2DBCRelationalFunctionCallTest {

    private static Expression[] serializeCollection(String... tokens) {
        Expression[] rv = new Expression[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            rv[i] = ConstantImpl.create(tokens[i]);
        }
        return rv;
    }

    private static class TokenizeFunction extends RelationalFunctionCall<String> {
        final PathBuilder<String> alias;
        final StringPath token;

        public TokenizeFunction(String alias, String... tokens) {
            super(String.class, "tokenize", serializeCollection(tokens));
            this.alias = new PathBuilder<String>(String.class, alias);
            this.token = Expressions.stringPath(this.alias, "token");
        }

    }

//    TODO: move R2DBCExpressions to R2DBC Expressions
//    @Test
//    public void validation() {
//        QSurvey survey = QSurvey.survey;
//        TokenizeFunction func = new TokenizeFunction("func", "a", "b");
//        R2DBCQuery<?> sub = R2DBCExpressions.selectOne().from(func.as(func.alias)).where(survey.name.like(func.token));
//        System.out.println(sub);
//    }

    @Test
    public void noArgs() {
        R2DBCRelationalFunctionCall<String> functionCall = R2DBCExpressions.relationalFunctionCall(String.class, "getElements");
        assertEquals("getElements()", functionCall.getTemplate().toString());
    }

    @Test
    public void twoArgs() {
        StringPath str = Expressions.stringPath("str");
        R2DBCRelationalFunctionCall<String> functionCall = R2DBCExpressions.relationalFunctionCall(String.class, "getElements", "a", str);
        assertEquals("getElements({0}, {1})", functionCall.getTemplate().toString());
        assertEquals("a", functionCall.getArg(0));
        assertEquals(str, functionCall.getArg(1));
    }

}
