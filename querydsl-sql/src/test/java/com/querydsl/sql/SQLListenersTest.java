package com.querydsl.sql;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLDetailedListener;
import com.querydsl.sql.SQLListener;
import com.querydsl.sql.SQLListenerContext;
import com.querydsl.sql.SQLListenerContextImpl;
import com.querydsl.sql.SQLListeners;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertThat;

public class SQLListenersTest
{

    @Test
    public void NotifyQuery()
    {
        SQLListener listener = createMock(SQLListener.class);
        SQLListeners listeners = new SQLListeners();
        listeners.add(listener);

        QueryMetadata md = new DefaultQueryMetadata();
        listener.notifyQuery(md);
        replay(listener);

        listeners.notifyQuery(md);
        verify(listener);
    }

    @Test
    public void NotifyQuery_Parent()
    {
        SQLListener listener = createMock(SQLListener.class);
        SQLListeners listeners = new SQLListeners(listener);

        QueryMetadata md = new DefaultQueryMetadata();
        listener.notifyQuery(md);
        replay(listener);

        listeners.notifyQuery(md);
        verify(listener);
    }


    @Test
    public void NotifyQuery_DetailedListener_start()
    {
        SQLListenerContext sqlListenerContext = createMock(SQLListenerContext.class);
        SQLDetailedListener listenerParent = createMock(SQLDetailedListener.class);
        SQLDetailedListener listener1 = createMock(SQLDetailedListener.class);
        SQLDetailedListener listener2 = createMock(SQLDetailedListener.class);

        listenerParent.start(sqlListenerContext);
        replay(listenerParent);

        listener1.start(sqlListenerContext);
        replay(listener1);

        listener2.start(sqlListenerContext);
        replay(listener2);


        SQLListeners listeners = new SQLListeners(listenerParent);
        listeners.add(listener1);
        listeners.add(listener2);

        listeners.start(sqlListenerContext);
        verify(listenerParent);
        verify(listener1);
        verify(listener2);
    }


    @Test
    public void NotifyQuery_DetailedListener_contexSetting()
    {
        SQLListenerContext sqlListenerContext = new SQLListenerContextImpl(new DefaultQueryMetadata());
        SQLDetailedListener listenerParent = new AssertingDetailedListener("keyParent", "valueParent");
        SQLDetailedListener listener1 = new AssertingDetailedListener("key1", "value1");
        SQLDetailedListener listener2 = new AssertingDetailedListener("key1", "value1");

        SQLListeners listeners = new SQLListeners(listenerParent);
        listeners.add(listener1);
        listeners.add(listener2);

        listeners.start(sqlListenerContext);
        listeners.preRender(sqlListenerContext);
        listeners.rendered(sqlListenerContext);
        listeners.prePrepare(sqlListenerContext);
        listeners.prepared(sqlListenerContext);
        listeners.preExecute(sqlListenerContext);
        listeners.preExecute(sqlListenerContext);
    }


    static class AssertingDetailedListener implements SQLDetailedListener
    {
        private final String key;
        private final Object value;

        AssertingDetailedListener(final String key, final Object value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public void start(final SQLListenerContext context)
        {
            context.setData(key, value);
        }

        @Override
        public void preRender(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void rendered(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void prePrepare(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void prepared(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void preExecute(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void executed(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void exception(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void end(final SQLListenerContext context)
        {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void notifyQuery(final QueryMetadata md)
        {
        }

        @Override
        public void notifyDelete(final RelationalPath<?> entity, final QueryMetadata md)
        {
        }

        @Override
        public void notifyDeletes(final RelationalPath<?> entity, final List<QueryMetadata> batches)
        {
        }

        @Override
        public void notifyMerge(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> keys, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery)
        {
        }

        @Override
        public void notifyMerges(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLMergeBatch> batches)
        {
        }

        @Override
        public void notifyInsert(final RelationalPath<?> entity, final QueryMetadata md, final List<Path<?>> columns, final List<Expression<?>> values, final SubQueryExpression<?> subQuery)
        {
        }

        @Override
        public void notifyInserts(final RelationalPath<?> entity, final QueryMetadata md, final List<SQLInsertBatch> batches)
        {
        }

        @Override
        public void notifyUpdate(final RelationalPath<?> entity, final QueryMetadata md, final List<Pair<Path<?>, Expression<?>>> updates)
        {
        }

        @Override
        public void notifyUpdates(final RelationalPath<?> entity, final List<SQLUpdateBatch> batches)
        {
        }
    }

}
