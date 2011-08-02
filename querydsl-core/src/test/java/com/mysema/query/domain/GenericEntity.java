package com.mysema.query.domain;

import java.io.Serializable;

public class GenericEntity<KeyType extends Serializable, T> {
    
    private KeyType id;

    public KeyType getId() {
        return id;
    }

    public void setId(KeyType id) {
        this.id = id;
    }
    
}
