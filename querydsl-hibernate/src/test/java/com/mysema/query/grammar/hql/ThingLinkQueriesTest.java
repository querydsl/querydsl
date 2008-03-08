package com.mysema.query.grammar.hql;

import org.junit.Test;

import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.Domain2;
import static com.mysema.query.grammar.HqlGrammar.*;

/**
 * ThingLinkQueriesTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ThingLinkQueriesTest extends HqlQueryBase<ComplexQueriesTest>{
    
    private Domain2.Association a = new Domain2.Association("a");
    private Domain2.Tag g = new Domain2.Tag("g");
    private Domain2.Thing h = new Domain2.Thing("h");
    private Domain2.Thing t = new Domain2.Thing("t");
    
    @Test
    public void testQuery1(){
//        "select g._keyword, count(g._keyword) from " + Thing.class.getName()
//        + " h inner join h._tags as g where h._code in" + "(select t._code from "
//        + Association.class.getName() + " a " + "inner join a._thing as t "
//        + "where a._association = :association "
//        + "and t._isHidden = :hidden) group by g._keyword order by count(g._keyword) desc");
        
//        select(g._keyword, count(g._keyword))
//        .from(h).innerJoin(h._tags.as(g))
//        .where(h._code.in(
//                select(t._code).from(a
        
    }
    
    public void testQuery2(){
//        "select g._keyword, count(g._keyword) from "
//        + Thing.class.getName()
//        + " h inner join h._tags as g where h._code in"
//        + "(select t._code from "
//        + Association.class.getName()
//        + " a "
//        + "inner join a._thing as t "
//        + "where a._association = :association "
//        + "and t._isHidden = :hidden and t._timeStamp > :lastweek) 
//        group by g._keyword order by count(g._keyword) desc");
    }

}
