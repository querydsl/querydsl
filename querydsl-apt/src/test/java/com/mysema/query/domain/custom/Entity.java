package com.mysema.query.domain.custom;

import java.util.List;
import java.util.Map;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class Entity {

    List<EmbeddedType> list;
    
    EmbeddedType2 embedded;
    
    Map<String, EmbeddedType3> map;
    
    String stringProperty;
    
}
