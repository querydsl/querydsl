package com.mysema.query.domain;

import com.mysema.query.annotations.QueryEmbeddable;

@QueryEmbeddable
public class MyEmbeddable implements Comparable<MyEmbeddable> {

    private int foo;

    public int compareTo(MyEmbeddable individualToCompare) {
        return -1;
    }
    
    public int getFoo() {
        return foo;
    }

    public void setFoo(int foo) {
        this.foo = foo;
    }

}