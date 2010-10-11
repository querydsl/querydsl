package com.mysema.query.jpa.domain2;

import javax.persistence.Embeddable;

@Embeddable
public class Child {

    private String childName;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    
}
