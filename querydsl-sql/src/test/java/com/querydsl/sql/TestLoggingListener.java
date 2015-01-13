package com.querydsl.sql;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.SQLDetailedListener;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLListenerContext;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;

import java.util.List;

import static java.lang.String.format;

/**
 */
public class TestLoggingListener implements SQLDetailedListener
{
    private static boolean enabled = false;

    /**
     * Called to enable logging in tests
     */
    public static void enable()
    {
        enabled = true;
    }

    /**
     * Called to disable logging in tests
     */
    public static void disable()
    {
        enabled = false;
    }

    @Override
    public void start(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\n\tstart %s", context));
    }

    @Override
    public void preRender(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\t\tpreRender %s", context));
    }

    @Override
    public void rendered(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\t\t\trendered %s", context));
    }

    @Override
    public void prePrepare(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\t\tprePrepare %s", context));
    }

    @Override
    public void prepared(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\t\t\tprepared %s", context));
    }

    @Override
    public void preExecute(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\t\tpreExecute %s", context));
    }

    @Override
    public void executed(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\t\t\texecuted %s", context));
    }

    @Override
    public void exception(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\t\texception %s", context));
    }

    @Override
    public void end(final SQLListenerContext context)
    {
        if (enabled) System.out.println(format("\tend %s\n\n", context));
    }

    @Override
    public void notifyQuery(final QueryMetadata md)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyQuery %s", md));
    }

    @Override
    public void notifyDelete(final RelationalPath<?> entity, final QueryMetadata md)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyDelete %s", entity));
    }

    @Override
    public void notifyDeletes(final RelationalPath<?> entity, final List<QueryMetadata> batches)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyDeletes %s", entity));
    }

    @Override
    public void notifyMerge(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> keys, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyMerge %s", entity));
    }

    @Override
    public void notifyMerges(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLMergeBatch> batches)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyMerges %s", entity));
    }

    @Override
    public void notifyInsert(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyInsert %s", entity));
    }

    @Override
    public void notifyInserts(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLInsertBatch> batches)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyInserts %s", entity));
    }

    @Override
    public void notifyUpdate(final RelationalPath<?> entity, final QueryMetadata md, final List<Pair<Path<?>, Expression<?>>> updates)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyUpdate %s", entity));
    }

    @Override
    public void notifyUpdates(final RelationalPath<?> entity, final List<SQLUpdateBatch> batches)
    {
        if (enabled) System.out.println(format("\t\t\tnotifyUpdates %s", entity));
    }
}
