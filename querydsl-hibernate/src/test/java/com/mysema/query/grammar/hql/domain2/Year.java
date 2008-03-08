/**
 * 
 */
package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * 
 */
@Entity
public class Year {

    @Id
    private Integer _year;

    public Year() {
    }

    /**
     * @param year
     */
    public Year(int year) {
        _year = new Integer(year);
    }

    /**
     * @return
     */
    public Integer getYear() {
        return _year;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object year) {
        return ((Year) year).getYear().equals(_year);
    }

}
