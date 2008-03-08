package com.mysema.query.grammar.hql.domain2;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class GroupOfInterest {

    @Id
    private String _name;

    @OneToMany
    private List<User> _users;

    @OneToMany
    private List<Thing> _things;

    private GroupOfInterestPrivacy _privacyLevel;

    public enum GroupOfInterestPrivacy {
        PRIVATE, PUBLIC;
    }

    public GroupOfInterest() {
    }

    /**
     * @param name
     *            The name of the GroupOfInterest.
     */
    public GroupOfInterest(String name) {
        _name = name;
    }

    /**
     * @param user
     *            The User to add to the GroupOfInterest.
     */
    public void addUser(User user) {
        if (_users == null) {
            _users = new ArrayList<User>();
        }
        _users.add(user);
    }

    /**
     * @param thing
     *            The Thing to add to the GroupOfInterest.
     */
    public void addThing(Thing thing) {
        if (_things == null) {
            _things = new ArrayList<Thing>();
        }
        _things.add(thing);
    }

    /**
     * @return The name of the GroupOfInterest.
     */
    public String getName() {
        return _name;
    }

    /**
     * @return The privacy level of the GroupOfInterest.
     */
    public GroupOfInterestPrivacy getPrivacyLevel() {
        return _privacyLevel;
    }

    /**
     * @param privacyLevel
     *            Privacy-level of the GroupOfInterest.
     */
    public void setPrivacyLevel(GroupOfInterestPrivacy privacyLevel) {
        _privacyLevel = privacyLevel;
    }

    /**
     * @return All Users that belong to the GroupOfInterest.
     */
    public List<User> getUsers() {
        return _users;
    }

    /**
     * @return All Things that belong to the GroupOfInterest.
     */
    public List<Thing> getThings() {
        return _things;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object group) {
        return ((GroupOfInterest) group).getName().equals(_name);
    }
}
