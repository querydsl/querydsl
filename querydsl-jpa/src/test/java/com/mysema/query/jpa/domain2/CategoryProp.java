package com.mysema.query.jpa.domain2;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CategoryProp {

    @Id
    private long id;
    
    private long categoryId;
    
    private String propName;
    
    private String propValue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }
    
}
