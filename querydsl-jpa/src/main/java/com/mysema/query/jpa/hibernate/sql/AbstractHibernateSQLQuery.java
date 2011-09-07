package com.mysema.query.jpa.hibernate.sql;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.AbstractSQLQuery;
import com.mysema.query.jpa.NativeSQLSerializer;
import com.mysema.query.jpa.hibernate.DefaultSessionHolder;
import com.mysema.query.jpa.hibernate.FactoryExpressionTransformer;
import com.mysema.query.jpa.hibernate.HibernateUtil;
import com.mysema.query.jpa.hibernate.SessionHolder;
import com.mysema.query.jpa.hibernate.StatelessSessionHolder;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Path;

/**
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractHibernateSQLQuery<Q extends AbstractHibernateSQLQuery<Q>> extends AbstractSQLQuery<Q> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHibernateSQLQuery.class);

    protected Boolean cacheable, readOnly;

    protected String cacheRegion;

    @Nullable
    private Map<Object,String> constants;

    @Nullable
    private List<Path<?>> entityPaths;

    protected int fetchSize = 0;

    private final SessionHolder session;

    protected final SQLTemplates sqlTemplates;

    protected int timeout = 0;

    public AbstractHibernateSQLQuery(Session session, SQLTemplates sqlTemplates) {
        this(new DefaultSessionHolder(session), sqlTemplates, new DefaultQueryMetadata());
    }

    public AbstractHibernateSQLQuery(StatelessSession session, SQLTemplates sqlTemplates){
        this(new StatelessSessionHolder(session), sqlTemplates, new DefaultQueryMetadata());
    }

    public AbstractHibernateSQLQuery(SessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(metadata);
        this.session = session;
        this.sqlTemplates = sqlTemplates;
    }
    
    private String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        NativeSQLSerializer serializer = new NativeSQLSerializer(sqlTemplates);
        serializer.serialize(queryMixin.getMetadata(), forCountRow);
        constants = serializer.getConstantToLabel();
        entityPaths = serializer.getEntityPaths();
        return serializer.toString();
    }

    public Query createQuery(Expression<?>... args){
        queryMixin.getMetadata().setValidate(false);
        queryMixin.addToProjection(args);
        return createQuery(toQueryString());
    }

    @SuppressWarnings("unchecked")
    private Query createQuery(String queryString) {
        logQuery(queryString);
        org.hibernate.SQLQuery query = session.createSQLQuery(queryString);
        // set constants
        HibernateUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        // set entity paths
        for (Path<?> path : entityPaths){
            query.addEntity(path.toString(), path.getType());
        }
        // set result transformer, if projection is an EConstructor instance
        List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
        if (projection.size() == 1 && projection.get(0) instanceof FactoryExpression){
            query.setResultTransformer(new FactoryExpressionTransformer((FactoryExpression<?>) projection.get(0)));
        }
        if (fetchSize > 0){
            query.setFetchSize(fetchSize);
        }
        if (timeout > 0){
            query.setTimeout(timeout);
        }
        if (cacheable != null){
            query.setCacheable(cacheable);
        }
        if (cacheRegion != null){
            query.setCacheRegion(cacheRegion);
        }
        if (readOnly != null){
            query.setReadOnly(readOnly);
        }
        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> list(Expression<?>[] projection) {
        Query query = createQuery(projection);
        reset();
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        Query query = createQuery(projection);
        reset();
        return query.list();
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        return new IteratorAdapter<Object[]>(list(args).iterator());
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator());
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addToProjection(projection);
        Query query = createQuery(toCountRowsString());
        long total = ((Integer)query.uniqueResult()).longValue();
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            String queryString = toQueryString();
            query = createQuery(queryString);
            @SuppressWarnings("unchecked")
            List<RT> list = query.list();
            reset();
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            reset();
            return SearchResults.emptyResults();
        }
    }

    protected void logQuery(String queryString){
        if (logger.isDebugEnabled()){
            logger.debug(queryString.replace('\n', ' '));
        }
    }

    protected void reset() {
        queryMixin.getMetadata().reset();
        entityPaths = null;
        constants = null;
    }

    protected String toCountRowsString() {
        return buildQueryString(true);
    }

    protected String toQueryString(){
        return buildQueryString(false);
    }

    @Override
    public Object[] uniqueResult(Expression<?>[] args) {
        Query query = createQuery(args);
        Object obj = uniqueResult(query);
        if (obj != null){
            return obj.getClass().isArray() ? (Object[])obj : new Object[]{obj};
        }else{
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> RT uniqueResult(Expression<RT> expr) {
        Query query = createQuery(expr);
        return (RT)uniqueResult(query);
    }

    @Nullable
    private Object uniqueResult(Query query) {
        reset();
        try{
            return query.uniqueResult();
        }catch (org.hibernate.NonUniqueResultException e){
            throw new NonUniqueResultException();
        }
    }

    /**
     * Enable caching of this query result set.
     * @param cacheable Should the query results be cacheable?
     */
    @SuppressWarnings("unchecked")
    public Q setCacheable(boolean cacheable){
        this.cacheable = cacheable;
        return (Q)this;
    }

    /**
     * Set the name of the cache region.
     * @param cacheRegion the name of a query cache region, or <tt>null</tt>
     * for the default query cache
     */
    @SuppressWarnings("unchecked")
    public Q setCacheRegion(String cacheRegion){
        this.cacheRegion = cacheRegion;
        return (Q)this;
    }

    /**
     * Set a fetch size for the underlying JDBC query.
     * @param fetchSize the fetch size
     */
    @SuppressWarnings("unchecked")
    public Q setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return (Q)this;
    }

    /**
     * Entities retrieved by this query will be loaded in
     * a read-only mode where Hibernate will never dirty-check
     * them or make changes persistent.
     *
     */
    @SuppressWarnings("unchecked")
    public Q setReadOnly(boolean readOnly){
        this.readOnly = readOnly;
        return (Q)this;
    }

    /**
     * Set a timeout for the underlying JDBC query.
     * @param timeout the timeout in seconds
     */
    @SuppressWarnings("unchecked")
    public Q setTimeout(int timeout){
        this.timeout = timeout;
        return (Q)this;
    }

}
