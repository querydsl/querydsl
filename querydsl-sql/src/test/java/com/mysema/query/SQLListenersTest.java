package com.mysema.query;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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

}
