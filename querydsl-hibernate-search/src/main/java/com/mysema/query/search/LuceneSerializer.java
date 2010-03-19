/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.Path;

public class LuceneSerializer {
    private final boolean lowerCase;

    public LuceneSerializer(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    private Query toOrQuery(Operation<?, ?> operation) {
        return toTwoHandSidedQuery(operation, Occur.SHOULD);
    }

    private Query toAndQuery(Operation<?, ?> operation) {
        return toTwoHandSidedQuery(operation, Occur.MUST);
    }

    private Query toTwoHandSidedQuery(Operation<?, ?> operation, Occur occur) {
        // TODO Flatten(?)
        Query lhs = toQuery(operation.getArg(0));
        Query rhs = toQuery(operation.getArg(1));
        BooleanQuery bq = new BooleanQuery();
        bq.add(lhs, occur);
        bq.add(rhs, occur);
        return bq;
    }

    private Query toNotQuery(Operation<?, ?> operation) {
        // TODO Flatten(?)
        BooleanQuery bq = new BooleanQuery();
        bq.add(new BooleanClause(toQuery(operation.getArg(0)), Occur.MUST_NOT));
        return bq;
    }

    private Query toPhraseQuery(Operation<?, ?> operation, String[] terms) {
        PhraseQuery pq = new PhraseQuery();
        for (String term : terms) {
            pq.add(new Term(toField((PString) operation.getArg(0)), lowerCase ? term.toLowerCase() : term));
        }
        return pq;
    }

    private Query toQuery(Operation<?, ?> operation) {
        Operator<?> op = operation.getOperator();
        if (op == Ops.OR) {
            return toOrQuery(operation);

        } else if (op == Ops.AND) {
            return toAndQuery(operation);

        } else if (op == Ops.LIKE) {
            if (!(operation.getArg(1) instanceof Constant<?>)) {
                throw new IllegalArgumentException("operation argument was not of type Constant.");
            }
            String term = operation.getArg(1).toString();
            String[] terms = StringUtils.split(term);
            if (terms.length > 1) {
                toPhraseQuery(operation, terms);
            }
            return new WildcardQuery(new Term(toField((PString) operation.getArg(0)), lowerCase ? term.toLowerCase() : term));
        } else if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE) {
            if (!(operation.getArg(1) instanceof Constant<?>)) {
                throw new IllegalArgumentException("operation argument was not of type Constant.");
            }
            String term = operation.getArg(1).toString();
            String[] terms = StringUtils.split(term);
            if (terms.length > 1) {
                return toPhraseQuery(operation, terms);
            }
            return new TermQuery(new Term(toField((PString) operation.getArg(0)), lowerCase ? term.toLowerCase() : term));

        } else if (op == Ops.NOT) {
            return toNotQuery(operation);
        } else if (op == Ops.STARTS_WITH) {
            if (!(operation.getArg(1) instanceof Constant<?>)) {
                throw new IllegalArgumentException("operation argument was not of type Constant.");
            }
            String[] terms = StringUtils.split(operation.getArg(1).toString());
            if (terms.length > 1) {
                return toPhraseQuery(operation, terms);
            }
            String term = operation.getArg(1).toString();
            return new PrefixQuery(new Term(toField((PString) operation.getArg(0)), lowerCase ? term.toLowerCase() : term));
        } else if (op == Ops.STRING_CONTAINS) {
            // TODO This is basically a special case of Ops.LIKE *...*
            if (!(operation.getArg(1) instanceof Constant<?>)) {
                throw new IllegalArgumentException("operation argument was not of type Constant.");
            }
            String term = QueryParser.escape(operation.getArg(1).toString());
            String[] terms = StringUtils.split(term);
            if (terms.length > 1) {
                toPhraseQuery(operation, terms);
            }
            return new WildcardQuery(new Term(toField((PString) operation.getArg(0)), "*" + (lowerCase ? term.toLowerCase() : term) + "*"));
        } else if (op == Ops.ENDS_WITH) {
            // TODO Speacial case of Ops.LIKE *...
            if (!(operation.getArg(1) instanceof Constant<?>)) {
                throw new IllegalArgumentException("operation argument was not of type Constant.");
            }
            String term = QueryParser.escape(operation.getArg(1).toString());
            String[] terms = StringUtils.split(term);
            if (terms.length > 1) {
                toPhraseQuery(operation, terms);
            }
            return new WildcardQuery(new Term(toField((PString) operation.getArg(0)), "*" + (lowerCase ? term.toLowerCase() : term)));
        }
        throw new UnsupportedOperationException();
    }

    public Query toQuery(Expr<?> expr) {
        if (expr instanceof Operation<?, ?>) {
            return toQuery((Operation<?, ?>) expr);
        }
        throw new IllegalArgumentException("expr was not of type Operation");
    }

    public String toField(Path<?> path) {
        return path.getMetadata().getExpression().toString();
    }
}
