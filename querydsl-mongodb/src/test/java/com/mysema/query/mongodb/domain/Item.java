package com.mysema.query.mongodb.domain;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class Item {
    
    private @Id ObjectId id;
    
    private List<ObjectId> ctds;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<ObjectId> getCtds() {
        return ctds;
    }

    public void setCtds(List<ObjectId> ctds) {
        this.ctds = ctds;
    }
    

}
