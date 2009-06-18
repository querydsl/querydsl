package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.jdoql.models.fitness.QGym;
import com.mysema.query.jdoql.models.fitness.Wardrobe;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.Expr;


/**
 * Tests for JDOQL queries of collections and maps.
 */
public class JDOQLContainerTest {

    private QGym gym = QGym.gym1;

    private Wardrobe wrd = new Wardrobe(), wrd1 = new Wardrobe(), wrd2 = new Wardrobe();
    
    @Before
    public void setUp(){
        wrd.setModel("model");
    }
    
    /**
     * Tests NOT contains in Map.values
     */
    @Test
    public void testNotContainsValuesInMapFields() {        

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "WHERE !this.wardrobes.containsValue(a1) " +
          "PARAMETERS com.mysema.query.jdoql.models.fitness.Wardrobe a1",
          
          serialize(query().from(gym)
                  .where(gym.wardrobes.containsValue(wrd).not()).listExpr(gym)));
        

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) && !this.wardrobes.containsValue(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
        
        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "WHERE !this.wardrobes.containsValue(a1) && !this.wardrobes.containsValue(a2) " +
          "PARAMETERS com.mysema.query.jdoql.models.fitness.Wardrobe a1, com.mysema.query.jdoql.models.fitness.Wardrobe a2",
          
          serialize(query().from(gym)
                  .where(gym.wardrobes.containsValue(wrd).not(), gym.wardrobes.containsValue(wrd2).not())
                  .listExpr(gym)));
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsValue(wrd) && !this.wardrobes.containsValue(wrd2) && this.wardrobes.containsValue(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "WHERE !this.wardrobes.containsValue(a1) && !this.wardrobes.containsValue(a2) && this.wardrobes.containsValue(a3) " +
          "PARAMETERS com.mysema.query.jdoql.models.fitness.Wardrobe a1, com.mysema.query.jdoql.models.fitness.Wardrobe a2, com.mysema.query.jdoql.models.fitness.Wardrobe a3",
                
        serialize(query().from(gym)
                .where(
                    gym.wardrobes.containsValue(wrd).not(), 
                    gym.wardrobes.containsValue(wrd1).not(),
                    gym.wardrobes.containsValue(wrd2))
                .listExpr(gym)));
    }


    /**
     * Tests NOT contains in Map.keys
     */
    @Test public void testNotContainsKeysInMapFields() {

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "WHERE !this.wardrobes2.containsKey(a1) " +
          "PARAMETERS com.mysema.query.jdoql.models.fitness.Wardrobe a1",
                
          serialize(query().from(gym)
                   .where(gym.wardrobes2.containsKey(wrd).not()).listExpr(gym)));
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) && !this.wardrobes2.containsKey(wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "WHERE !this.wardrobes2.containsKey(a1) && !this.wardrobes2.containsKey(a2) " +
          "PARAMETERS com.mysema.query.jdoql.models.fitness.Wardrobe a1, com.mysema.query.jdoql.models.fitness.Wardrobe a2",
                
          serialize(query().from(gym)
                   .where(
                       gym.wardrobes2.containsKey(wrd).not(),
                       gym.wardrobes2.containsKey(wrd2).not())
                   .listExpr(gym)));
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes2.containsKey(wrd) && !this.wardrobes2.containsKey(wrd2) && this.wardrobes2.containsKey(wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");
        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "WHERE !this.wardrobes2.containsKey(a1) && !this.wardrobes2.containsKey(a2) && this.wardrobes2.containsKey(a3) " +
          "PARAMETERS com.mysema.query.jdoql.models.fitness.Wardrobe a1, com.mysema.query.jdoql.models.fitness.Wardrobe a2, com.mysema.query.jdoql.models.fitness.Wardrobe a3",
                      
        serialize(query().from(gym)
                 .where(
                     gym.wardrobes2.containsKey(wrd).not(),
                     gym.wardrobes2.containsKey(wrd2).not(),
                     gym.wardrobes2.containsKey(wrd1))
                 .listExpr(gym)));
    }

    /**
     * Tests NOT contains in Map.entry
     */
    @Test public void testNotContainsEntryInMapFields() {
        // NOTE : containsEntry is not supported in Querydsl

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) && !this.wardrobes.containsEntry(wrd2.model,wrd2) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2");
//
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE !this.wardrobes.containsEntry(wrd.model,wrd) && !this.wardrobes.containsEntry(wrd2.model,wrd2) && this.wardrobes.containsEntry(wrd1.model,wrd1) "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd,org.jpox.samples.models.fitness.Wardrobe wrd2,org.jpox.samples.models.fitness.Wardrobe wrd1");

    }


    /**
     * Tests get
     */
    @Test public void testGetInMapFields() {        

//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "WHERE this.wardrobes.get(wrd.model) == wrd "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "WHERE this.wardrobes.get(a1) == a2 " +
          "PARAMETERS java.lang.String a1, com.mysema.query.jdoql.models.fitness.Wardrobe a2",
                      
          serialize(query().from(gym)
                   .where(gym.wardrobes.get(wrd.getModel()).eq(wrd)).listExpr(gym)));
    }

    /**
     * Tests get method used in ordering
     */
    @Test public void testGetInOrderingInMapFields() {
//        "SELECT FROM org.jpox.samples.models.fitness.Gym "
//                + "PARAMETERS org.jpox.samples.models.fitness.Wardrobe wrd");
//        .setOrdering("this.wardrobes.get(wrd.model).model ascending");
        assertEquals(
          "SELECT this FROM com.mysema.query.jdoql.models.fitness.Gym " +
          "ORDER BY this.wardrobes.get(a1).model ASC",
                            
          serialize(query().from(gym)
                   .orderBy(gym.wardrobes(wrd.getModel()).model.asc()).listExpr(gym)));
    }

    private JDOQLQuery query(){
        // creates detached query
        return new JDOQLQueryImpl(null);
    }

    private String serialize(SubQuery expr) {
        Expr<?> source = expr.getMetadata().getJoins().get(0).getTarget();
        JDOQLSerializer serializer = new JDOQLSerializer(JDOQLPatterns.DEFAULT, source);
        serializer.serialize(expr.getMetadata(), false, false);
        String rv = serializer.toString().replace('\n', ' ');
//        System.out.println(rv);
        return rv;
    }
}