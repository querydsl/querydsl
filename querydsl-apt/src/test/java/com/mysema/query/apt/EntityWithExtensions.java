package com.mysema.query.apt;

import java.sql.Date;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class EntityWithExtensions {

    String id;
    
    Date date;
    
}
