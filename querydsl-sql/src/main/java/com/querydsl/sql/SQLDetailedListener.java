package com.querydsl.sql;

/**
 * An extended listener interface that details much more about the preparation and execution of queries
 */
public interface SQLDetailedListener extends SQLListener {
    /**
     * Called at the start of a querydsl.  Most context parameters are empty at this stage
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void start(SQLListenerContext context);

    /**
     * Called at the start of SQL rendering.
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void preRender(SQLListenerContext context);

    /**
     * Called at the end of SQL rendering.  The sql context value will not be available
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void rendered(SQLListenerContext context);

    /**
     * Called at the start of {@link java.sql.PreparedStatement} preparation.
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void prePrepare(SQLListenerContext context);

    /**
     * Called at the end of {@link java.sql.PreparedStatement} preparation.
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void prepared(SQLListenerContext context);

    /**
     * Called at the start of {@link java.sql.PreparedStatement} execution.
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void preExecute(SQLListenerContext context);

    /**
     * Called at the end of {@link java.sql.PreparedStatement} execution.
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void executed(SQLListenerContext context);

    /**
     * Called if an exception happens during querydsl building and execution.  The context exception values will
     * now be available indicating the exception that occurred.
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void exception(SQLListenerContext context);

    /**
     * Called at the end of a querydsl.
     *
     * @param context a context object that is progressively filled out as the querydsl executes
     */
    void end(SQLListenerContext context);
}
