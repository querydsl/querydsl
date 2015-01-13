package com.querydsl.codegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.types.OrderSpecifier;


public class Examples {
    
    public static class Supertype {
        
        public String supertypeProperty;
    }
    
    @QueryEntity
    public static class SimpleEntity extends Supertype{

    }

    @QueryEntity
    public static abstract class AbstractEntity<Id extends java.io.Serializable> {

        public Id id;

        public String first;

    }

    @QueryEntity
    public static class SubEntity extends AbstractEntity<java.lang.Long> {
     
        public String second;
     
    } 
    

    @QueryEntity
    public static class ComplexCollections {
    
        @QueryEmbedded
        public List<Complex<String>> list;
        
        @QueryEmbedded
        public Map<String, Complex<String>> map;
        
        @QueryEmbedded
        public Map<String, Complex<?>> map2;
        
        @QueryEmbedded
        public Map<?, Complex<String>> map3;
                
                
    }
    
    
    public static class Complex<T extends Comparable<T>> implements Comparable<Complex<T>> {

        public T a;
        
        @Override
        public int compareTo(Complex<T> arg0) {
            return 0;
        }
        
        public boolean equals(Object o) {
            return o == this;
        }
    }
    
    @QueryEntity
    public static class Reference {

    }

    @QueryEntity
    public static class GenericRelations{
        public Collection<Collection<Reference>> col1;
        public Collection<List<Reference>> col2;
        public Collection<Collection<? extends Reference>> col3;
        public Collection<List<? extends Reference>> col4;

        public Set<List<Reference>> set1;
        public Set<Collection<Reference>> set2;
        public Set<List<? extends Reference>> set3;
        public Set<Collection<? extends Reference>> set4;

        public Map<String,List<String>> map1;
        public Map<List<String>,String> map2;
        public Map<String,List<? extends String>> map3;
        public Map<List<? extends String>,String> map4;
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
