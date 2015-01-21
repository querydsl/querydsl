package com.querydsl.apt.domain;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;

public class Generic7Test {

    @QueryEntity
    public static class AbstractCollectionAttribute<T extends Collection<?>> {

        T value;

    }

    @QueryEntity
    public static class ListAttribute<T> extends AbstractCollectionAttribute<List<T>> {

        String name;

    }

    @QueryEntity
    public static class Product {

        ListAttribute<Integer> integerAttributes;
        ListAttribute<String> stringAttributes;

    }
    
    @Test
    public void test() {
        
    }
    
}
