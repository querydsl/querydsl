package com.querydsl.sql;

import static java.lang.String.format;

import java.util.List;
import java.util.Map;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLMergeUsingCase;
import com.querydsl.sql.dml.SQLUpdateBatch;

/**
 */
public class TestLoggingListener implements SQLDetailedListener {
    private static boolean enabled = false;

    /**
     * Called to enable logging in tests
     */
    public static void enable() {
        enabled = true;
    }

    /**
     * Called to disable logging in tests
     */
    public static void disable() {
        enabled = false;
    }

    @Override
    public void start(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\n\tstart %s", context));
        }
    }

    @Override
    public void preRender(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\t\tpreRender %s", context));
        }
    }

    @Override
    public void rendered(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\t\t\trendered %s", context));
        }
    }

    @Override
    public void prePrepare(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\t\tprePrepare %s", context));
        }
    }

    @Override
    public void prepared(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\t\t\tprepared %s", context));
        }
    }

    @Override
    public void preExecute(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\t\tpreExecute %s", context));
        }
    }

    @Override
    public void executed(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\t\t\texecuted %s", context));
        }
    }

    @Override
    public void exception(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\t\texception %s", context));
        }
    }

    @Override
    public void end(SQLListenerContext context) {
        if (enabled) {
            System.out.println(format("\tend %s\n\n", context));
        }
    }

    @Override
    public void notifyQuery(QueryMetadata md) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyQuery %s", md));
        }
    }

    @Override
    public void notifyDelete(RelationalPath<?> entity, QueryMetadata md) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyDelete %s", entity));
        }
    }

    @Override
    public void notifyDeletes(RelationalPath<?> entity, List<QueryMetadata> batches) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyDeletes %s", entity));
        }
    }

    @Override
    public void notifyMerge(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> keys, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyMerge %s", entity));
        }
    }

    @Override
    public void notifyMerges(RelationalPath<?> entity, QueryMetadata md, List<SQLMergeBatch> batches) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyMerges %s", entity));
        }
    }

    @Override
    public void notifyMergeUsing(RelationalPath<?> entity, QueryMetadata md, SimpleExpression<?> usingExpression, Predicate usingOn, List<SQLMergeUsingCase> whens) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyMergeUsing %s", entity));
        }
    }

    @Override
    public void notifyInsert(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyInsert %s", entity));
        }
    }

    @Override
    public void notifyInserts(RelationalPath<?> entity, QueryMetadata md, List<SQLInsertBatch> batches) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyInserts %s", entity));
        }
    }

    @Override
    public void notifyUpdate(RelationalPath<?> entity, QueryMetadata md, Map<Path<?>, Expression<?>> updates) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyUpdate %s", entity));
        }
    }

    @Override
    public void notifyUpdates(RelationalPath<?> entity, List<SQLUpdateBatch> batches) {
        if (enabled) {
            System.out.println(format("\t\t\tnotifyUpdates %s", entity));
        }
    }
}
