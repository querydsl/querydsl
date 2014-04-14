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
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.annotation.Nullable;

import org.hibernate.hql.internal.ast.HqlParser;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;

class QueryHelper extends JPAQueryBase<QueryHelper> {

    public QueryHelper(JPQLTemplates templates) {
        this(new DefaultQueryMetadata(), templates);
    }
    
    public QueryHelper(QueryMetadata metadata, JPQLTemplates templates) {
        super(metadata, templates);
    }

    @Override
    protected JPQLSerializer createSerializer() {
        return new JPQLSerializer(getTemplates());
    }

    public long count() {
        return 0;
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        throw new UnsupportedOperationException();
    }
    
    @Nullable
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        throw new UnsupportedOperationException();
    }

    public void parse() throws RecognitionException, TokenStreamException {
        try {
            String input = toString();
            System.out.println("input: " + input.replace('\n', ' '));
            HqlParser parser = HqlParser.getInstance(input);
            parser.setFilter(false);
            parser.statement();
            AST ast = parser.getAST();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            parser.showAst(ast, new PrintStream(baos));
            assertEquals("At least one error occurred during parsing " + input,
                    0, parser.getParseErrorHandler().getErrorCount());
        } finally {
            // clear();
            System.out.println();
        }
    }

    public QueryHelper select(Expression<?>... exprs) {
        queryMixin.addProjection(exprs);
        return this;
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> projection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public QueryHelper clone() {
        return new QueryHelper(getMetadata().clone(), getTemplates());
    }
    
}
