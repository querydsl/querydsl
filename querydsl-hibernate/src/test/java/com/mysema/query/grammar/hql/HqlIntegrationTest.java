package com.mysema.query.grammar.hql;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.runner.RunWith;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.mysema.query.hibernate.QueryUtil;
import com.mysema.query.util.HibernateTestRunner;


/**
 * HibernatePersistenceTest provides
 *
 * @author tiwe
 * @version $Id$
 */
@RunWith(HibernateTestRunner.class)
public class HqlIntegrationTest extends HqlParserTest{
    
    private Session session;
        
    @Override
    protected void parse() throws RecognitionException, TokenStreamException{
        System.out.println("query : " + toString().replace('\n', ' '));
        // create Query and execute it
        Query query = session.createQuery(toString());
        QueryUtil.setConstants(query, getConstants());
        try{
            query.list();    
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }        
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}
