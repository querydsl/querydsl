/**
 * 
 */
package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * An Association represents a relationship between a Thing an a User. For
 * example a User can like, hate or sell a Thing. (see thinglink-2.0 datamodel)
 * 
 * 
 */
@Entity
// @Table(uniqueConstraints={@UniqueConstraint(columnNames={ "user", "thing",
// "association" })})
public class Association extends TimeStamped {

    @OneToOne
    private User _user;

    @OneToOne
    private Thing _thing;

    private AssociationType _association;

    public enum AssociationType {
        LIKE, COLLECT, MAKE, OWN, SWAP, GIVE, HATE, SELL, WANT, LOVE, LINKED;
    }

    public Association() {
    }

    /**
     * @param user
     *            The User the Association is related to.
     * @param thing
     *            The Thing the Association is related to.
     * @param association
     *            The type of the Association.
     */
    public Association(User user, Thing thing, AssociationType association) {
        _user = user;
        _thing = thing;
        _association = association;
    }

    /**
     * @return The User the Association is related to.
     */
    public User getUser() {
        return _user;
    }

    /**
     * @return The Thing the Association is related to.
     */
    public Thing getThing() {
        return _thing;
    }

    /**
     * @return The type of the Association.
     */
    public AssociationType getAssociation() {
        return _association;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object association) {
        boolean sameUser = ((Association) association).getUser().equals(_user);
        boolean sameThing = ((Association) association).getThing().equals(_thing);
        boolean sameType = ((Association) association).getAssociation().equals(_association);
        return (sameUser && sameThing && sameType);
    }

}
