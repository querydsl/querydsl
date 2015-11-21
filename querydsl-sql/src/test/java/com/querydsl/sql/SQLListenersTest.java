package com.querydsl.sql;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.dml.SQLInsertBatch;
import com.querydsl.sql.dml.SQLMergeBatch;
import com.querydsl.sql.dml.SQLUpdateBatch;

public class SQLListenersTest {

    @Test
    public void notifyQuery() {
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
    public void notifyQuery_parent() {
        SQLListener listener = createMock(SQLListener.class);
        SQLListeners listeners = new SQLListeners(listener);

        QueryMetadata md = new DefaultQueryMetadata();
        listener.notifyQuery(md);
        replay(listener);

        listeners.notifyQuery(md);
        verify(listener);
    }


    @Test
    public void notifyQuery_detailedListener_start() {
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
    public void notifyQuery_detailedListener_contexSetting() {
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

    static class AssertingDetailedListener implements SQLDetailedListener {
        private final String key;
        private final Object value;

        AssertingDetailedListener(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void start(SQLListenerContext context) {
            context.setData(key, value);
        }

        @Override
        public void preRender(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void rendered(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void prePrepare(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void prepared(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void preExecute(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void executed(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void exception(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void end(SQLListenerContext context) {
            assertThat(this.value, CoreMatchers.equalTo(context.getData(key)));
        }

        @Override
        public void notifyQuery(QueryMetadata md) {
        }

        @Override
        public void notifyDelete(RelationalPath<?> entity, QueryMetadata md) {
        }

        @Override
        public void notifyDeletes(RelationalPath<?> entity, List<QueryMetadata> batches) {
        }

        @Override
        public void notifyMerge(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> keys, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {
        }

        @Override
        public void notifyMerges(RelationalPath<?> entity, QueryMetadata md, List<SQLMergeBatch> batches) {
        }

        @Override
        public void notifyInsert(RelationalPath<?> entity, QueryMetadata md, List<Path<?>> columns, List<Expression<?>> values, SubQueryExpression<?> subQuery) {
        }

        @Override
        public void notifyInserts(RelationalPath<?> entity, QueryMetadata md, List<SQLInsertBatch> batches) {
        }

        @Override
        public void notifyUpdate(RelationalPath<?> entity, QueryMetadata md, Map<Path<?>, Expression<?>> updates) {
        }

        @Override
        public void notifyUpdates(RelationalPath<?> entity, List<SQLUpdateBatch> batches) {
        }
    }

}
