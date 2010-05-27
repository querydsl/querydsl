/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * @author tiwe
 *
 */
public class Projections {
    
    private final Module module;
    
    private final Target target;

    public Projections(Module module, Target target) {
        this.module = module;
        this.target = target;
    }
    
    public <A> Collection<Expr<?>> array(EArray<A> expr, EArray<A> other, A knownElement){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        if (!module.equals(Module.RDFBEAN)){
            rv.add(expr.size());    
        }        
        return rv;
    }

    public <A> Collection<Expr<?>> collection(ECollection<?,A> expr, ECollection<?,A> other, A knownElement){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        if (!module.equals(Module.RDFBEAN)){
            rv.add(expr.size());    
        }        
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Expr<?>> date(EDate<A> expr, EDate<A> other, A knownValue){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(new PDate<A>(expr.getType(), PathMetadataFactory.forDelegate(expr)));
        rv.add(expr.dayOfMonth());
        rv.add(expr.month());
        rv.add(expr.year());
        rv.add(expr.yearMonth());
        
        if (module != Module.COLLECTIONS && module != Module.RDFBEAN){
            rv.add(expr.min());
            rv.add(expr.max());         
        }
        
        return rv;
    } 

    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Expr<?>> dateTime(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(new PDateTime<A>(expr.getType(), PathMetadataFactory.forDelegate(expr)));
        rv.add(expr.dayOfMonth());
        rv.add(expr.month());
        rv.add(expr.year());
        rv.add(expr.yearMonth());
        rv.add(expr.hour());
        rv.add(expr.minute());
        rv.add(expr.second());
        
        if (module != Module.COLLECTIONS && module != Module.RDFBEAN){
            rv.add(expr.min());
            rv.add(expr.max());         
        }
        
        return rv;
    }

    public <A> Collection<Expr<?>> list(EList<A> expr, EList<A> other, A knownElement){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.get(0));
        if (!module.equals(Module.RDFBEAN)){
            rv.add(expr.size());    
        }        
        return rv;
    }

    public <K,V> Collection<Expr<?>> map(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue) {
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.get(knownKey));
        if (!module.equals(Module.RDFBEAN)){
            rv.add(expr.size());    
        }        
        return rv;
    }

    public <A extends Number & Comparable<A>> Collection<ENumber<?>> numeric(ENumber<A> expr, ENumber<A> other, A knownValue, boolean forFilter){
        HashSet<ENumber<?>> rv = new HashSet<ENumber<?>>();
        rv.addAll(numeric(expr, other, forFilter));
        rv.addAll(numeric(expr, ENumberConst.create(knownValue), forFilter));
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    private <A extends Number & Comparable<A>> Collection<ENumber<?>> numeric(ENumber<A> expr, ENumber<?> other, boolean forFilter){
        HashSet<ENumber<?>> rv = new HashSet<ENumber<?>>();
        rv.add(new PNumber<A>(expr.getType(), PathMetadataFactory.forDelegate(expr)));
        rv.add(expr.abs());
        rv.add(expr.add(other));
        rv.add(expr.divide(other));
        
        rv.add(expr.multiply(other));
        rv.add(expr.sqrt());
        rv.add(expr.subtract(other));
        
        if (!forFilter && module != Module.COLLECTIONS && module != Module.RDFBEAN){
            rv.add(expr.min());
            rv.add(expr.max());
            rv.add(expr.avg());
            rv.add(expr.count());
            rv.add(expr.countDistinct());
        }
                
        if (!(other instanceof Constant || module == Module.JDOQL || module == Module.RDFBEAN)){
            CaseBuilder cases = new CaseBuilder();
            rv.add(ENumberConst.create(1).add(cases
                .when(expr.gt(10)).then(expr)
                .when(expr.between(0, 10)).then((ENumber)other)
                .otherwise((ENumber)other)));    
            
            rv.add(expr
                    .when((ENumber)other).then(expr)
                    .otherwise((ENumber)other));
        }
        
        
        return rv;
    }
    
    public <A extends Number & Comparable<A>> Collection<ENumber<?>> numericCasts(ENumber<A> expr, ENumber<A> other, A knownValue){
        if (!target.equals(Target.MYSQL)){
            HashSet<ENumber<?>> rv = new HashSet<ENumber<?>>();
            rv.add(expr.byteValue());
            rv.add(expr.doubleValue());
            rv.add(expr.floatValue());
            rv.add(expr.intValue());
            rv.add(expr.longValue());
            rv.add(expr.shortValue());     
            return rv;    
        }else{
            return Collections.emptySet();
        }        
    }

    public Collection<Expr<String>> string(EString expr, EString other, String knownValue){
        HashSet<Expr<String>> rv = new HashSet<Expr<String>>();
        rv.addAll(stringProjections(expr, other));
        rv.addAll(stringProjections(expr, EStringConst.create(knownValue)));
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    public Collection<Expr<String>> stringProjections(EString expr, EString other){
        HashSet<Expr<String>> rv = new HashSet<Expr<String>>();
        rv.add(new PString(PathMetadataFactory.forDelegate(expr)));
        
        rv.add(expr.append("Hello"));
        rv.add(expr.append(other));
            
        rv.add(expr.concat(other));
        rv.add(expr.concat("Hello"));
           
        rv.add(expr.lower());
            
        rv.add(expr.prepend("Hello"));
        rv.add(expr.prepend(other));
            
        rv.add(expr.stringValue());
            
        rv.add(expr.substring(1));
        rv.add(expr.substring(0, 1));
                
        if (!(other instanceof Constant || module == Module.JDOQL || module == Module.RDFBEAN)){
            CaseBuilder cases = new CaseBuilder();
            rv.add(cases.when(expr.eq("A")).then(other)
                        .when(expr.eq("B")).then(expr)
                        .otherwise(other));    
            
            rv.add(expr.when("A").then(other)
                       .when("B").then(expr)
                       .otherwise(other));
        }         
          
        rv.add(expr.trim());
            
        rv.add(expr.upper());                       
        return rv;
    }
        
    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Expr<?>> time(ETime<A> expr, ETime<A> other, A knownValue){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(new PTime<A>(expr.getType(), PathMetadataFactory.forDelegate(expr)));
        rv.add(expr.hour());
        rv.add(expr.minute());
        rv.add(expr.second());
        return rv;
    }
        
}
