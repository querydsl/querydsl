package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;

public abstract class TestFilters {

    // Path
    
    private static <A> Collection<EBoolean> getFiltersForPath(Path<A> expr, Path<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
             expr.isNull(),
             expr.isNotNull()
        );
    }
        
    // Expr
    
    private static <A> Collection<EBoolean> getFiltersForExpr(Expr<A> expr, Expr<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(other),
            expr.eq(knownValue),
            expr.ne(other),
            expr.ne(knownValue)
        );
    }
    
    // EComparable
    
    private static <A extends Comparable<A>> Collection<EBoolean> getFiltersForComparable(EComparable<A> expr, EComparable<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(getFiltersForExpr(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
            expr.after(other),
            expr.after(knownValue),
            expr.aoe(other),
            expr.aoe(knownValue),
            expr.before(other),
            expr.before(knownValue),
            expr.boe(other),
            expr.boe(knownValue)
        ));
        return rv;
    }
        
    // EBoolean
    
    public static Collection<EBoolean> getFiltersForBoolean(EBoolean expr, EBoolean other){
        return Arrays.asList(
            expr.and(other),
            expr.or(other),
            expr.not().and(other.not()),
            expr.not(),
            other.not()
        );
    }
    
    // ENumber
    
    @SuppressWarnings("unchecked")
    public static <A extends Number & Comparable<A>> Collection<EBoolean> getFiltersForNumber(ENumber<A> expr, ENumber<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();        
        for (ENumber<?> num : TestExprs.getProjectionsForNumber(expr, other, knownValue)){
            rv.add(num.lt(expr));
        }        
        rv.addAll(Arrays.asList(
            expr.goe(other),
            expr.goe(knownValue),
            expr.gt(other),
            expr.gt(knownValue),            
            expr.loe(other),
            expr.loe(knownValue),
            expr.lt(other),
            expr.lt(knownValue)
            
        ));       
        if (expr.getType().equals(Integer.class)){
            ENumber<Integer> eint = (ENumber)expr;
            rv.add(eint.between(1, 2));
            rv.add(eint.notBetween(1, 2));            
        }else if (expr.getType().equals(Double.class)){
            ENumber<Double> edouble = (ENumber)expr;
            rv.add(edouble.between(1.0, 2.0));
            rv.add(edouble.notBetween(1.0, 2.0));
        }
        
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    public static Collection<EBoolean> getFiltersForString(EString expr, EString other, String knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        if (expr instanceof Path && other instanceof Path){
            rv.addAll(getFiltersForPath((Path<String>)expr, (Path<String>)other, knownValue));
        }
        rv.addAll(getFiltersForComparable(expr, other, knownValue));        
        for (EString eq : TestExprs.getProjectionsForString(expr, other, knownValue)){
            rv.add(eq.eq(other));
        }
        rv.addAll(Arrays.<EBoolean>asList(            
            expr.between("A", "Z"),
            expr.charAt(0).eq(knownValue.charAt(0)),
            expr.notBetween("A", "Z"),
            expr.contains(other),
            expr.contains(knownValue.substring(0,1)),
            expr.endsWith(other),
            expr.endsWith(knownValue.substring(1)),            
            expr.equalsIgnoreCase(other),
            expr.equalsIgnoreCase(knownValue),
            expr.in(Arrays.asList(knownValue)),
            expr.indexOf(other).gt(0),
            expr.indexOf("X", 1).gt(0),
            expr.indexOf(knownValue).gt(0),
            expr.in("A","B","C"),
            expr.lastIndexOf("X").gt(0),
            expr.lastIndexOf(other).gt(0),
            expr.like(knownValue.substring(0,1)+"%"),
            expr.like("%"+knownValue.substring(1)),
            expr.like("%"+knownValue.substring(1,2)+"%"),
            expr.length().gt(0),            
            expr.notBetween("A", "Z"),
            expr.notBetween(other, other)
        ));
        return rv;
    }
    
    public static Collection<EBoolean> getMatchingFilters(EString expr, EString other, String knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(other),
            expr.eq(knownValue),
            expr.ne(other),
            expr.ne(knownValue),
            expr.equalsIgnoreCase(other),
            expr.equalsIgnoreCase(knownValue),
            expr.lower().eq(other.lower()),
            expr.upper().eq(other.upper()),
            expr.lower().eq(knownValue.toLowerCase()),
            expr.charAt(0).eq(other.charAt(0)),
            expr.endsWith(other),
            expr.endsWith(knownValue),
            expr.startsWith(other),
            expr.startsWith(knownValue),
            expr.contains(other),
            expr.contains(knownValue),
            other.startsWith(expr),
            other.endsWith(expr),
            other.contains(expr),
            expr.substring(0,1).eq(other.substring(0,1)),
            expr.substring(1).eq(other.substring(1)),
            expr.substring(0,1).eq(knownValue.substring(0,1)),
            expr.substring(1).eq(knownValue.substring(1)),
            expr.like(knownValue),
            other.like(knownValue)
        );
    }
    
    public static <A extends Number & Comparable<A>> Collection<EBoolean> getMatchingFilters(ENumber<A> expr, ENumber<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(knownValue),
            expr.goe(knownValue),
            expr.gt(knownValue.intValue()-1),
            expr.loe(knownValue),
            expr.lt(knownValue.intValue()+1)
        );
    }
    
    private TestFilters(){}
    
}
