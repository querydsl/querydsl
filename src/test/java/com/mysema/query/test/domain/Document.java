package com.mysema.query.test.domain;

import java.util.Date;

/**
 * Document provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Document {
    private String name;
    private Date validTo;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    
    
}
