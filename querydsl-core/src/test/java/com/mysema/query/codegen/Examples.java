package com.mysema.query.codegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.OrderSpecifier;


public class Examples {
    
    public static class Supertype {
        
        String supertypeProperty;
    }
    
    @QueryEntity
    public static class SimpleEntity extends Supertype{

    }

    @QueryEntity
    public static abstract class AbstractEntity<Id extends java.io.Serializable> {

        Id id;

        String first;

    }

    @QueryEntity
    public static class SubEntity extends AbstractEntity<java.lang.Long> {
     
        String second;
     
    } 
    

    @QueryEntity
    public static class ComplexCollections {
    
        @QueryEmbedded
        List<Complex<String>> list;
        
        @QueryEmbedded
        Map<String, Complex<String>> map;
        
        @QueryEmbedded
        Map<String, Complex<?>> map2;
        
        @QueryEmbedded
        Map<?, Complex<String>> map3;
                
                
    }
    
    
    public static class Complex<T extends Comparable<T>> implements Comparable<Complex<T>> {

        T a;
        
        @Override
        public int compareTo(Complex<T> arg0) {
            return 0;
        }
        
    }
    
    @QueryEntity
    public static class Reference {

    }

    @QueryEntity
    public static class GenericRelations{
        Collection<Collection<Reference>> col1;
        Collection<List<Reference>> col2;
        Collection<Collection<? extends Reference>> col3;
        Collection<List<? extends Reference>> col4;

        Set<List<Reference>> set1;
        Set<Collection<Reference>> set2;
        Set<List<? extends Reference>> set3;
        Set<Collection<? extends Reference>> set4;

        Map<String,List<String>> map1;
        Map<List<String>,String> map2;
        Map<String,List<? extends String>> map3;
        Map<List<? extends String>,String> map4;
    }

    @QueryEntity
    public static class Subtype extends DefaultQueryMetadata{

        private static final long serialVersionUID = -218949941713252847L;

    }
    
    @QueryEntity
    public static class OrderBys {
        
        List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();
        
    }
    
    @QueryEntity
    public static class SimpleTypes {
        
        List<Class<? extends Date>> classList5;
    }
}
