package com.mysema.query.mongodb.domain2;

import java.util.Map;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

@Entity(value = "USER", noClassnameStored = true)
public class User {
        
    @Embedded
    Map<String, UserAttribute> properties;

}