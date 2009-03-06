/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.collections.MiniApi.from;
import static com.mysema.query.collections.MiniApi.reject;
import static com.mysema.query.collections.MiniApi.select;
import static com.mysema.query.grammar.Grammar.gt;
import static com.mysema.query.grammar.GrammarWithAlias.$;
import static com.mysema.query.grammar.GrammarWithAlias.alias;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.GrammarWithAlias;
import com.mysema.query.grammar.QMath;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Path.PEntity;

/**
 * ColQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ColQueryTest extends AbstractQueryTest{
    private TestQuery last;
    
    private TestQuery query(){
        last = new TestQuery();
        return last;
    }
    
    @Before public void setUp(){
        myInts.add(1);
        myInts.add(2);
        myInts.add(3);
        myInts.add(4);        
        
        GrammarWithAlias.resetAlias();
    }
    
    @Test
    public void testAfterAndBefore(){
        query().from(cat, c1, c2).where(
                cat.birthdate.before(new Date()),
                cat.birthdate.boe(new Date()),
                cat.birthdate.after(new Date()),
                cat.birthdate.aoe(new Date())).list(cat);
    }
    
    @Test
    public void testAlias1(){
        // 1st
        QCat cat = new QCat("cat");  
        for (String name : from(cat,cats).where(cat.kittens.size().gt(0))
                          .iterate(cat.name)){
            System.out.println(name);
        }        
        
        // 1st - variation 1        
        for (String name : from(cat,cats).where(gt(cat.kittens.size(),0))
                          .iterate(cat.name)){
            System.out.println(name);
        }        
        
        // 2nd
        Cat c = alias(Cat.class, "cat");
        for (String name : from(c,cats).where($(c.getKittens()).size().gt(0))
                          .iterate(c.getName())){
            System.out.println(name);
        }
        
        // 2nd - variation 1
        for (String name : from(c,cats).where($(c.getKittens().size()).gt(0))
                          .iterate(c.getName())){
            System.out.println(name);
        }                
                            
    }
    
    @Test
    public void testAlias10(){
        Cat c = alias(Cat.class, "cat");
        
        try{
            from(c,cats).where($(c.getMate().getName().toUpperCase()).eq("MOE"));
            fail("expected NPE");
        }catch(NullPointerException ne){
            // expected
        }
        
    }
    
    @Test
    public void testAlias2(){        
        query().from(cat, c1, c2).from(otherCat, c2, c3)
            .where(cat.name.eq(otherCat.name)).select(cat.name);
        
        // 1st
        QCat cat = new QCat("cat");  
        for (String name : from(cat,cats).where(cat.name.like("fri%"))
                          .iterate(cat.name)){
            System.out.println(name);
        }
        
        // 2nd
        Cat c = alias(Cat.class, "cat");        
        for (String name : from(c,cats).where($(c.getName()).like("fri%"))
                          .iterate(c.getName())){
            System.out.println(name);
        }      
    }
    
    @Test
    public void testAlias3(){
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where($(c.getBirthdate()).after(new Date()))
        .iterate(c).iterator();              
    }
    
    @Test
    @Ignore
    public void testAlias4(){        
        Cat c = alias(Cat.class, "cat");
        
        // TODO : FIXME : Janino compiler doesn't handle generic collections
        from(c,cats)
        .where($(c.getKittens().get(0).getBodyWeight()).gt(12))
        .iterate(c.getName()).iterator();
    }
    
    @Test
    public void testAlias5(){
        Cat c = alias(Cat.class, "cat");
        Cat other = new Cat();
        
        from(c,cats)
        .where($(c).eq(other))
        .iterate(c).iterator();
    }
    
    @Test
    public void testAlias6(){        
        new QCat("cat").kittens.contains(new QCat("other"));
        
        Cat c = alias(Cat.class, "cat");
        Cat other = new Cat();
        
        from(c,cats)
        .where($(c.getKittens().contains(other)))
        .iterate(c).iterator();
    }
    
    @Test
    public void testAlias7(){
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where(c.getKittens().isEmpty())
        .iterate(c).iterator();
    }
    
    @Test
    public void testAlias8(){
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where($(c.getName()).startsWith("B"))
        .iterate(c).iterator();        
    }
    

    @Test
    public void testAlias9(){
        Cat c = alias(Cat.class, "cat");
        
        from(c,cats)
        .where($(c.getName()).toUpperCase().eq("MOE"))
        .iterate(c).iterator();        
    }
    
    @Test
    @Ignore
    public void testAliasToString(){
        // NOTE : temporarily commented out, since alias features have been moved to querydsl-core
        Cat c = alias(Cat.class, "c");
        
        assertEquals("c", c.toString());
        assertEquals("c.getMate()", c.getMate().toString());
        assertEquals("c.getMate().getKittens().get(0)", c.getMate().getKittens().get(0).toString());
        
        assertEquals("c.getKittens().get(0)", c.getKittens().get(0).toString());
        assertEquals("c.getKittens().get(1)", c.getKittens().get(1).toString());
        assertEquals("c.getKittens().get(0).getMate()", c.getKittens().get(0).getMate().toString());        
    }

    @Test
    public void testAPIMethods(){
        query().from(cat, c1, c2).list(cat);
        query().from(cat, c1, c2).iterate(cat).iterator();
    }
    
    @Test
    public void testArrayProjection(){
        // select pairs of cats with different names
        query().from(cat,cats).from(otherCat,cats)
            .where(cat.name.ne(otherCat.name)).select(cat.name, otherCat.name);
        assertTrue(last.res.size() == 4 * 3);
    }
    
    @Test
    public void testCast(){
        ENumber<?> num = cat.id;
        Expr<?>[] expr = new Expr[]{
                num.byteValue(), 
                num.doubleValue(),
                num.floatValue(),
                num.intValue(),
                num.longValue(),
                num.shortValue(),
                num.stringValue()};
        
        for (Expr<?> e : expr){
            query().from(cat, c1, c2).list(e);    
        }        
                
    }    
@Test
    public void testCSVIteration(){       
        List<String> lines = Arrays.asList("1;10;100","2;20;200","3;30;300");
        
        // 1st
        for (String[] row : query().from($(""), lines).iterate($("").split(";"))){
            for (String col : row){
                System.out.println(col);
            }
        }
        
        // 2nd
        Path.PStringArray strs = $(new String[]{});
        Iterable<String[]> csvData1 = query().from($(""), lines).iterate($("").split(";"));
        for (String s : query().from(strs, csvData1).iterate(strs.get(0).add("-").add(strs.get(1)))){
            System.out.println(s);
        }         
    }
    
    @Test
    public void testJoins(){
        // TOOD : coming soon!
//        query().from(cat, cats)
//            .innerJoin(otherCat, cats).on(cat.mate.eq(otherCat))
//            .select(cat, otherCat);
//        
//        query().from(cat, cats)
//            .innerJoin(otherCat, cats).on(cat.id.eq(otherCat.id))
//            .select(cat, otherCat);               
    }
         
    @Test
    public void testMapUsage(){
        // FIXME
        Map<String,String> map = new HashMap<String,String>();      
        map.put("1","one");
        map.put("2","two");
        map.put("3","three");
        map.put("4","four");
        
        // 1st 
        PEntity<Map.Entry<String,String>> e = $(map.entrySet().iterator().next());
        for (Map.Entry<String,String> entry : from(e, map.entrySet()).iterate(e)){
            System.out.println(entry.getKey() + " > " + entry.getValue());
        }
        
        // 2nd
//        for (String[] kv : from($("k"), $("v"), map).iterate($("k"),$("v"))){
//            System.out.println(kv[0] + " > " + kv[1]);
//        }
    }
    
    @SuppressWarnings("unchecked")
    @Test public void testMathFunctions(){
        Cat c = alias(Cat.class,"c");
        Expr<Integer> i = new Expr.EConstant<Integer>(1);
        Expr<Double> d = new Expr.EConstant<Double>(1.0);
        from(c, cats)
        .iterate(
                QMath.abs(i),
                QMath.acos(d),
                QMath.asin(d),
                QMath.atan(d),
                QMath.ceil(d),
                QMath.cos(d),
                QMath.tan(d),
                QMath.sqrt(i),
                QMath.sin(d),
                QMath.round(d),
                QMath.random(),
                QMath.pow(d,d),
                QMath.min(i,i),
                QMath.max(i,i),
                QMath.mod(i,i),
                QMath.log10(d),
                QMath.log(d),
                QMath.floor(d),
                QMath.exp(d)).iterator();
          
    }
        
    @Test
    public void testOrder(){
        query().from(cat,cats).orderBy(cat.name.asc()).select(cat.name);
        assertArrayEquals(new Object[]{"Alex","Bob","Francis","Kitty"}, last.res.toArray());
        
        query().from(cat,cats).orderBy(cat.name.desc()).select(cat.name);
        assertArrayEquals(new Object[]{"Kitty","Francis","Bob","Alex"}, last.res.toArray());
        
        query().from(cat,cats).orderBy(cat.name.substring(1).asc()).select(cat.name);
        assertArrayEquals(new Object[]{"Kitty","Alex","Bob","Francis"}, last.res.toArray());
        
        query().from(cat,cats).from(otherCat,cats)
            .orderBy(cat.name.asc(), otherCat.name.desc()).select(cat.name, otherCat.name);

        // TODO : more tests
    }
    
    @Test
    public void testPrimitives(){
        // select cats with kittens
        query().from(cat,cats).where(cat.kittens.size().ne(0)).select(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select cats without kittens
        query().from(cat,cats).where(cat.kittens.size().eq(0)).select(cat.name);
        assertTrue(last.res.size() == 0);
    }
    
    @Test
    public void testSimpleCases(){
        // select all cat names
        query().from(cat,cats).select(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select all kittens
        query().from(cat,cats).select(cat.kittens);
        assertTrue(last.res.size() == 4);
        
        // select cats with kittens
        query().from(cat,cats).where(cat.kittens.size().gt(0)).select(cat.name);
        assertTrue(last.res.size() == 4);
                
        // select cats named Kitty
        query().from(cat,cats).where(cat.name.eq("Kitty")).select(cat.name);
        assertTrue(last.res.size() == 1);
        
        // select cats named Kitt%
        query().from(cat,cats).where(cat.name.like("Kitt%")).select(cat.name);
        assertTrue(last.res.size() == 1);        
        
        query().from(cat,cats).select(QMath.add(cat.bodyWeight, cat.weight));        
    }
    
    @Test public void testSimpleReject() {
    //  Iterable<Integer> oneAndTwo = reject(myInts, greaterThan(2));
        Iterable<Integer> oneAndTwo = reject(myInts, $(0).gt(2));
        
        for (Integer i : oneAndTwo) ints.add(i);
        assertEquals(Arrays.asList(1,2), ints);
    }
    
    @Test public void testSimpleSelect() {
    //  Iterable<Integer> threeAndFour = select(myInts, greaterThan(2));
        Iterable<Integer> threeAndFour = select(myInts, $(0).gt(2));  
        
        for (Integer i : threeAndFour) ints.add(i);
        assertEquals(Arrays.asList(3,4), ints);
    }

    @Test
    public void testStringHandling(){
        Iterable<String> data1 = Arrays.asList("petER", "THomas", "joHAN");
        Iterable<String> data2 = Arrays.asList("PETer", "thOMAS", "JOhan");
        
        Iterator<String> res = Arrays.asList("petER - PETer","THomas - thOMAS", "joHAN - JOhan").iterator();
        for (Object[] arr : query().from($("a"), data1).from($("b"), data2).where($("a").equalsIgnoreCase($("b"))).iterate($("a"),$("b"))){
            assertEquals(res.next(), arr[0]+" - "+arr[1]);
        }
    }
    
    @Test
    public void testVarious(){
        for(Object[] strs : from($("a"), "aa","bb","cc").from($("b"), "a","b")
                .where($("a").startsWith($("b")))
                .iterate($("a"),$("b"))){
            System.out.println(Arrays.asList(strs));
        }
        
        query().from(cat,cats).select(cat.mate);
        
        query().from(cat,cats).select(cat.kittens);
        
        query().from(cat,cats).where(cat.name.like("fri%")).select(cat.name);
        
//        select(mother, offspr, mate.name).from(mother)
//            .innerJoin(mother.mate.as(mate)).leftJoin(mother.kittens.as(offspr)).parse();

//        select(new QFamily(mother, mate, offspr))
//            .from(mother).innerJoin(mother.mate.as(mate))
//            .leftJoin(mother.kittens.as(offspr)).parse();
        
    }
    
    @Test
    public void testVarious1(){
        for(String s : from($("str"), "a","ab","cd","de")
                .where($("str").startsWith("a"))
                .iterate($("str"))){
            assertTrue(s.equals("a") || s.equals("ab"));
            System.out.println(s);
        }
    }
    
    @Test
    public void testVarious2(){
        for (Object o : from($(),1,2,"abc",5,3).where($().ne("abc")).iterate($())){
            int i = (Integer)o;
            assertTrue(i > 0 && i < 6);
            System.out.println(o);
        }                
    }
    
    @Test
    public void testVarious3(){
        for (Integer i : from($(0),1,2,3,4).where($(0).lt(4)).iterate($(0))){
            System.out.println(i);
        }
    }
    
    @Test
    public void testCats(){
        EBoolean where = cat.name.like("Bob5%").and(otherCat.name.like("Kate5%"));
        int size = 100;
        List<Cat> cats1 = new ArrayList<Cat>(size);
        for (int i= 0; i < size; i++){
            cats1.add(new Cat("Bob" + i));
        }
        List<Cat> cats2 = new ArrayList<Cat>(size);
        for (int i=0; i < size; i++){
            cats2.add(new Cat("Kate" + i));
        }
        
        ColQuery query = new ColQuery().from(cat, cats1).from(otherCat, cats2);
        query.setWrapIterators(false);
        for (Object[] objects : MiniApi.from(cat, cats1).from(otherCat, cats2).where(where).iterate(cat, otherCat)){
            System.out.println(Arrays.asList(objects));
        }
                   
    }
    
    private static class TestQuery extends AbstractColQuery<TestQuery>{
        List<Object> res = new ArrayList<Object>();
        <RT> void select(Expr<RT> projection){
            for (Object o : iterate(projection)){
                System.out.println(o);
                res.add(o);
            }
            System.out.println();
        }
        <RT> void select(Expr<RT> p1, Expr<RT> p2, Expr<RT>... rest){
            for (Object[] o : iterate(p1, p2, rest)){
                System.out.println(Arrays.asList(o));
                res.add(o);
            }
            System.out.println();
        }
    }
    

}
