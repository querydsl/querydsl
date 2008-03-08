package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A Thing can have multiple Tags.
 * 
 * 
 */
@Entity
public class Tag {

    @Id
    private String _keyword;

    public Tag() {
    }

    /**
     * @param keyword
     *            Keyword of the Tag.
     */
    public Tag(String keyword) {
        _keyword = keyword;
    }

    /**
     * @return Keyword of the Tag.
     */
    public String getKeyword() {
        return _keyword;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object tag) {
        return ((Tag) tag).getKeyword().equals(_keyword);
    }
}
