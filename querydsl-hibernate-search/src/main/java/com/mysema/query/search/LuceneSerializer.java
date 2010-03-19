package com.mysema.query.search;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.Path;

public class LuceneSerializer {
    private static final char WILDCARD_ALL = '*';

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
            String[] terms = StringUtils.split(operation.getArg(1).toString());
            if (terms.length > 1) {
                toPhraseQuery(operation, terms);
            }
            String term = operation.getArg(1).toString();
            return new WildcardQuery(new Term(toField((PString) operation.getArg(0)), WILDCARD_ALL + (lowerCase ? term.toLowerCase() : term) + WILDCARD_ALL));
        } else if (op == Ops.EQ_OBJECT || op == Ops.EQ_PRIMITIVE) {
            String[] terms = StringUtils.split(operation.getArg(1).toString());
            if (terms.length > 1) {
                return toPhraseQuery(operation, terms);
            }
            String term = operation.getArg(1).toString();
            return new TermQuery(new Term(toField((PString) operation.getArg(0)), lowerCase ? term.toLowerCase() : term));
        } else if (op == Ops.NOT) {
            return toNotQuery(operation);
        } else if (op == Ops.STARTS_WITH) {
            String[] terms = StringUtils.split(operation.getArg(1).toString());
            if (terms.length > 1) {
                return toPhraseQuery(operation, terms);
            }
            String term = operation.getArg(1).toString();
            return new PrefixQuery(new Term(toField((PString) operation.getArg(0)), lowerCase ? term.toLowerCase() : term));
        }
        throw new UnsupportedOperationException();
    }

    public Query toQuery(Expr<?> expr) {
        if (expr instanceof Operation<?, ?>) {
            return toQuery((Operation<?, ?>) expr);
        }
        throw new IllegalArgumentException();
    }

    public String toField(Path<?> path) {
        return path.getMetadata().getExpression().toString();
    }
}
