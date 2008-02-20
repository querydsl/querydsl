package com.mysema.query.test.domain;


/**
 * Company provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Company {
//    public final NumberProperty id = num("id");
//    public final StringProperty name = str("name");
    private long id;
    private String name;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
}
