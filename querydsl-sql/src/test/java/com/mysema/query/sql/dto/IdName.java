package com.mysema.query.sql.dto;

/**
 * IAndName provides
 *
 * @author tiwe
 * @version $Id$
 */
public class IdName {
    
    private int id;
    
    private String name;
    
    public IdName(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    

}
