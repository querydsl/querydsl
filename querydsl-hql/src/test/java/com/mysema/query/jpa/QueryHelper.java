/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.annotation.Nullable;

import org.hibernate.hql.ast.HqlParser;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.HQLQueryBase;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.types.Expression;

class QueryHelper extends HQLQueryBase<QueryHelper> {

    public QueryHelper() {
        super(new DefaultQueryMetadata(), HQLTemplates.DEFAULT);
    }

    public long count() {
        return 0;
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
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
        getQueryMixin().addToProjection(exprs);
        return this;
    }
}
