/**
 * 
 */
package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A Thing has one or more Makers. A Maker can be featured. (see thinglink-2.0
 * datamodel)
 * 
 * 
 */
@Entity
public class Maker extends FeaturedItem {

    @Column(unique = true)
    private String _name;

    public Maker() {
    }

    /**
     * Calls super() to create timeStamp.
     */
    public Maker(String name) {
        super();
        _name = name;
    }

    /**
     * @return Name of the Maker.
     */
    public String getName() {
        return _name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object maker) {
        return ((Maker) maker).getName().equals(_name);
    }
}
