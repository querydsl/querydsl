package com.mysema.query.codegen;

import com.mysema.query.annotations.QueryEmbeddable;

@QueryEmbeddable
public class ExampleEmbeddable {

    private String property;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }



}
