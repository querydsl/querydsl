/**
 * 
 */
package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A Thing can belong to multiple Countries. The Countries specify where a Thing
 * comes from, whereas the {@link Location} specifies, where the Thing currently
 * is located.
 */
@Entity
public class Country {

    @Id
    private String _name;

    public Country() {
    }

    /**
     * @param name
     *            Name of the country.
     */
    public Country(String name) {
        _name = name;
    }

    /**
     * @return Name of the country.
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
    public boolean equals(Object country) {
        return ((Country) country).getName().equals(_name);
    }
}
