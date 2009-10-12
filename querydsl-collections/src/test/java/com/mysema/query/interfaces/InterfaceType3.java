package com.mysema.query.interfaces;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public interface InterfaceType3 extends InterfaceType, InterfaceType2{
    
    public String getProp2();

}
