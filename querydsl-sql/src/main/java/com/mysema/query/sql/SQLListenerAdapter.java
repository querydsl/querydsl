package com.mysema.query.sql;

import com.mysema.commons.lang.Pair;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.dml.SQLInsertBatch;
import com.mysema.query.sql.dml.SQLMergeBatch;
import com.mysema.query.sql.dml.SQLUpdateBatch;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;

import java.util.List;

/**
 * A simple adapter class that knows if the underlying listener is a simple or detailed SQL listener
 */
class SQLListenerAdapter implements SQLDetailedListener
{

    private final SQLListener sqlListener;
    private final SQLDetailedListener SQLDetailedListener;

    SQLListenerAdapter(final SQLListener sqlListener)
    {
        this.SQLDetailedListener = sqlListener instanceof SQLDetailedListener ? (SQLDetailedListener) sqlListener : null;
        this.sqlListener = sqlListener;
    }

    public SQLListener getSqlListener()
    {
        return sqlListener;
    }

    @Override
    public void start(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.start(context);
        }
    }

    @Override
    public void preRender(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.preRender(context);
        }
    }

    @Override
    public void rendered(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.rendered(context);
        }
    }

    @Override
    public void prePrepare(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.prePrepare(context);
        }
    }

    @Override
    public void prepared(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.prepared(context);
        }
    }

    @Override
    public void preExecute(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.preExecute(context);
        }
    }

    @Override
    public void executed(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.executed(context);
        }
    }

    @Override
    public void end(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.end(context);
        }
    }

    @Override
    public void exception(final SQLListenerContext context)
    {
        if (SQLDetailedListener != null)
        {
            SQLDetailedListener.exception(context);
        }
    }

    @Override
    public void notifyQuery(final QueryMetadata md)
    {
        sqlListener.notifyQuery(md);
    }

    @Override
    public void notifyDelete(final RelationalPath<?> entity, final QueryMetadata md)
    {
        sqlListener.notifyDelete(entity, md);
    }

    @Override
    public void notifyDeletes(final RelationalPath<?> entity, final List<QueryMetadata> batches)
    {
        sqlListener.notifyDeletes(entity, batches);
    }

    @Override
    public void notifyMerge(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> keys, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery)
    {
        sqlListener.notifyMerge(entity, md, keys, columns, values, subQuery);
    }

    @Override
    public void notifyMerges(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLMergeBatch> batches)
    {
        sqlListener.notifyMerges(entity, md, batches);
    }

    @Override
    public void notifyInsert(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery)
    {
        sqlListener.notifyInsert(entity, md, columns, values, subQuery);
    }

    @Override
    public void notifyInserts(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLInsertBatch> batches)
    {
        sqlListener.notifyInserts(entity, md, batches);
    }

    @Override
    public void notifyUpdate(final RelationalPath<?> entity, final QueryMetadata md, final List<Pair<Path<?>, Expression<?>>> updates)
    {
        sqlListener.notifyUpdate(entity, md, updates);
    }

    @Override
    public void notifyUpdates(final RelationalPath<?> entity, final List<SQLUpdateBatch> batches)
    {
        sqlListener.notifyUpdates(entity, batches);
    }
}
