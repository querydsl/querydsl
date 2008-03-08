package com.mysema.query.grammar.hql.domain2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Exhibition extends FeaturedItem {

    @Column(unique = true)
    private String _name;

    @OneToOne
    private User _user;

    @ManyToMany
    private List<Thing> _things;

    private String _location;

    private String _host;

    private Date _startDate;

    private Date _endDate;

    public Exhibition() {
    }

    /**
     * @param name
     *            The name of the Exhibition.
     * @param user
     *            The User responsible for the Exhibition.
     */
    public Exhibition(String name, User user) {
        _name = name;
        _user = user;
    }

    /**
     * @return The name of the Exhibition.
     */
    public String getName() {
        return _name;
    }

    /**
     * @return The User responsible for the Exhibition.
     */
    public User getUser() {
        return _user;
    }

    /**
     * @param thing
     *            The Thing to add to the Exhibition.
     */
    public void addThing(Thing thing) {
        if (_things == null)
            _things = new ArrayList<Thing>();
        _things.add(thing);
    }

    /**
     * @return All Things of the Exhibition.
     */
    public List<Thing> getThings() {
        return _things;
    }

    /**
     * @param location
     *            The location of the Exhibition.
     */
    public void setLocation(String location) {
        _location = location;
    }

    /**
     * @return The location of the Exhibition.
     */
    public String getLocation() {
        return _location;
    }

    /**
     * @param location
     *            The host of the Exhibition.
     */
    public void setHost(String host) {
        _host = host;
    }

    /**
     * @return The host of the Exhibition.
     */
    public String getHost() {
        return _host;
    }

    /**
     * @param startDate
     *            Date when the Exhibition begins.
     */
    public void setStartDate(Date startDate) {
        _startDate = startDate;
    }

    /**
     * @return Date when the Exhibition begins.
     */
    public Date getStartDate() {
        return _startDate;
    }

    /**
     * @param endDate
     *            Date when the Exhibition ends.
     */
    public void setEndDate(Date endDate) {
        _endDate = endDate;
    }

    /**
     * @return Date when the Exhibition ends.
     */
    public Date getEndDate() {
        return _endDate;
    }
    
    @Override
	public boolean equals(Object exhibition){
    	return ((Exhibition)exhibition).getName().equals(_name);
    }
}
