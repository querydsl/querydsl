package com.mysema.query.hql;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.grammar.HqlGrammar;
import com.mysema.query.grammar.HqlOps;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.types.Expr;

/**
 * AbstractHqlQuery provides the same features as HqlQuery, but acts as a super class
 * for domain specific query subclasses
 *
 * @author tiwe
 * @version $Id$
 */
public class AbstractHqlQuery<A extends AbstractHqlQuery<A>> extends HqlQueryBase<A> {
    
    private static final Logger logger = LoggerFactory.getLogger(HqlQuery.class);

    private static final HqlOps OPS_DEFAULT = new HqlOps();
    
    private final Session session;
    
    public AbstractHqlQuery(Session session) {
        this(session, OPS_DEFAULT);
    }

    public AbstractHqlQuery(Session session, HqlOps ops) {
        super(ops);
        this.session = session;
    }
    
    private Query createQuery(String queryString, Integer limit, Integer offset) {
        Query query = session.createQuery(queryString);
        setConstants(query, getConstants());        
        if (limit != null) query.setMaxResults(limit);
        if (offset != null) query.setFirstResult(offset);
        return query;
    }
    
    public static void setConstants(Query query, List<?> constants){
        for (int i=0; i < constants.size(); i++){
            String key = "a"+(i+1);
            Object val = constants.get(i);            
            if (val instanceof Collection<?>){
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key,(Collection<?>)val);
            }else if (val.getClass().isArray()){
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key,(Object[])val);
            }else{
                // NOTE : parameter types should be given explicitly
                query.setParameter(key,val);    
            }
        }
    }
        
    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr){
        select(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, limit, offset);
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        select(expr1, expr2);
        select(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, limit, offset);
        return query.list();
    }
    
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        select(expr);
        Query query = createQuery(toCountRowsString(), null, null);
        long total = (Long) query.uniqueResult();
        if (total > 0) {
            String queryString = toString();
            logger.debug("query : {}", queryString);
            query = createQuery(queryString, limit, offset);
            @SuppressWarnings("unchecked")
            List<RT> list = query.list();
            return new SearchResults<RT>(list,
                    limit == null ? Long.MAX_VALUE : limit.longValue(),
                    offset == null ? 0l : offset.longValue(), total);
        } else {
            return SearchResults.emptyResults();
        }
    }
    
    public long count(){
        return uniqueResult(HqlGrammar.count());
    }
    
    public long count(Expr<?> expr){
        return uniqueResult(HqlGrammar.count(expr));
    }
    
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        select(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, 1, null);
        return (RT)query.uniqueResult();
    }

}
