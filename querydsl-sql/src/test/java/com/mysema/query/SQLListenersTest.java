package com.mysema.query;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import com.mysema.query.sql.SQLDetailedListener;
import com.mysema.query.sql.SQLListenerContext;
import org.junit.Test;

import com.mysema.query.sql.SQLListener;
import com.mysema.query.sql.SQLListeners;

public class SQLListenersTest {

    @Test
    public void NotifyQuery() {
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
    public void NotifyQuery_Parent() {
        SQLListener listener = createMock(SQLListener.class);
        SQLListeners listeners = new SQLListeners(listener);

        QueryMetadata md = new DefaultQueryMetadata();
        listener.notifyQuery(md);
        replay(listener);

        listeners.notifyQuery(md);
        verify(listener);
    }


    @Test
    public void NotifyQuery_DetailedListener_start() {
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
}
