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
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.sql.domain.QSurvey;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.path.PathBuilder;
import com.querydsl.core.types.path.StringPath;

public class RelationalFunctionCallTest {
    
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
           this.token = new StringPath(this.alias, "token");        
       }
        
    }
    
    @Test
    public void Validation() {
        QSurvey survey = QSurvey.survey;
        TokenizeFunction func = new TokenizeFunction("func", "a", "b");
        SQLSubQuery sub = new SQLSubQuery().from(func.as(func.alias)).where(survey.name.like(func.token));
        System.out.println(sub);
        
    }
    
    @Test
    public void NoArgs() {
        RelationalFunctionCall<String> functionCall = RelationalFunctionCall.create(String.class, "getElements");
        assertEquals("getElements()", functionCall.getTemplate().toString());
    }
    
    @Test
    public void TwoArgs() {
        StringPath str = new StringPath("str");
        RelationalFunctionCall<String> functionCall = RelationalFunctionCall.create(String.class, "getElements", "a", str);
        assertEquals("getElements({0}, {1})", functionCall.getTemplate().toString());
        assertEquals("a", functionCall.getArg(0));
        assertEquals(str, functionCall.getArg(1));        
    }
    
}
