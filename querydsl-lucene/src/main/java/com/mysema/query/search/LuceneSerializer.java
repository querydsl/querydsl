/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;

/**
 * @author vema
 *
 */
public class LuceneSerializer {
    private final boolean lowerCase;

    public LuceneSerializer(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    private Query toQuery(Operation<?, ?> operation) {
        Operator<?> op = operation.getOperator();
        if (op == Ops.OR) {
            return toTwoHandSidedQuery(operation, Occur.SHOULD);
        } else if (op == Ops.AND) {
            return toTwoHandSidedQuery(operation, Occur.MUST);
        } else if (op == Ops.NOT) {
            BooleanQuery bq = new BooleanQuery();
            bq.add(new BooleanClause(toQuery(operation.getArg(0)), Occur.MUST_NOT));
            return bq;
        } else if (op == Ops.LIKE) {
            return like(operation);
        } else if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE) {
            return eq(operation);
        } else if (op == Ops.STARTS_WITH) {
            return startsWith(operation);
        } else if (op == Ops.STRING_CONTAINS) {
            return stringContains(operation);
        } else if (op == Ops.ENDS_WITH) {
            return endsWith(operation);
        } else if (op == Ops.BETWEEN) {
            return between(operation);
        }
        throw new UnsupportedOperationException();
    }

    private Query toTwoHandSidedQuery(Operation<?, ?> operation, Occur occur) {
        // TODO Flatten similar queries(?)
        Query lhs = toQuery(operation.getArg(0));
        Query rhs = toQuery(operation.getArg(1));
        BooleanQuery bq = new BooleanQuery();
        bq.add(lhs, occur);
        bq.add(rhs, occur);
        return bq;
    }

    private Query like(Operation<?, ?> operation) {
        verifyArguments(operation);
        String field = toField((Path<?>) operation.getArg(0));
        String[] terms = createTerms(operation.getArg(1));
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (String s : terms) {
                bq.add(new WildcardQuery(new Term(field, "*" + normalize(s) + "*")), Occur.MUST);
            }
            return bq;
        }
        return new WildcardQuery(new Term(field, normalize(terms[0])));
    }

    private Query eq(Operation<?, ?> operation) {
        verifyArguments(operation);
        String field = toField((Path<?>) operation.getArg(0));
        String[] terms = createTerms(operation.getArg(1));
        if (terms.length > 1) {
            PhraseQuery pq = new PhraseQuery();
            for (String s : terms) {
                pq.add(new Term(field, normalize(s)));
            }
            return pq;
        }
        return new TermQuery(new Term(field, normalize(terms[0])));
    }

    private Query startsWith(Operation<?, ?> operation) {
        verifyArguments(operation);
        String field = toField((Path<?>) operation.getArg(0));
        String[] terms = createEscapedTerms(operation.getArg(1));
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (int i = 0; i < terms.length; ++i) {
                String s = i == 0 ? terms[i] + "*" : "*" + terms[i] + "*";
                bq.add(new WildcardQuery(new Term(field, normalize(s))), Occur.MUST);
            }
            return bq;
        }
        return new PrefixQuery(new Term(field, normalize(terms[0])));
    }

    private Query stringContains(Operation<?, ?> operation) {
        verifyArguments(operation);
        String field = toField((Path<?>) operation.getArg(0));
        String[] terms = createEscapedTerms(operation.getArg(1));
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (String s : terms) {
                bq.add(new WildcardQuery(new Term(field, "*" + normalize(s) + "*")), Occur.MUST);
            }
            return bq;
        }
        return new WildcardQuery(new Term(field, "*" + normalize(terms[0]) + "*"));
    }

    private Query endsWith(Operation<?, ?> operation) {
        verifyArguments(operation);
        String field = toField((Path<?>) operation.getArg(0));
        String[] terms = createEscapedTerms(operation.getArg(1));
        if (terms.length > 1) {
            BooleanQuery bq = new BooleanQuery();
            for (int i = 0; i < terms.length; ++i) {
                String s = i == terms.length - 1 ? "*" + terms[i] : "*" + terms[i] + "*";
                bq.add(new WildcardQuery(new Term(field, normalize(s))), Occur.MUST);
            }
            return bq;
        }
        return new WildcardQuery(new Term(field, "*" + normalize(terms[0])));
    }

    private Query between(Operation<?, ?> operation) {
        verifyArguments(operation);
        // TODO Phrase not properly supported
        String field = toField((Path<?>) operation.getArg(0));
        String[] lowerTerms = createTerms(operation.getArg(1));
        String[] upperTerms = createTerms(operation.getArg(2));
        return new TermRangeQuery(field, normalize(lowerTerms[0]), normalize(upperTerms[0]), true,
                true);
    }

    public String toField(Path<?> path) {
        return path.getMetadata().getExpression().toString();
    }

    private void verifyArguments(Operation<?, ?> operation) {
        List<Expr<?>> arguments = operation.getArgs();
        for (int i = 1; i < arguments.size(); ++i) {
            if (!(arguments.get(i) instanceof Constant<?>)) {
                throw new IllegalArgumentException("operation argument was not of type Constant.");
            }
        }
    }

    private String[] createTerms(Expr<?> expr) {
        return StringUtils.split(expr.toString());
    }

    private String[] createEscapedTerms(Expr<?> expr) {
        return StringUtils.split(QueryParser.escape(expr.toString()));
    }

    private String normalize(String s) {
        return lowerCase ? s.toLowerCase(Locale.ENGLISH) : s;
    }

    public Query toQuery(Expr<?> expr) {
        if (expr instanceof Operation<?, ?>) {
            return toQuery((Operation<?, ?>) expr);
        }
        throw new IllegalArgumentException("expr was not of type Operation");
    }

}
