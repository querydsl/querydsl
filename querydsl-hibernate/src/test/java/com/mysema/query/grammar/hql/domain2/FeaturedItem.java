package com.mysema.query.grammar.hql.domain2;

import javax.persistence.MappedSuperclass;

/**
 * An item can be featured. If an item is featured, it has a weight and a
 * featuredTimeStamp. (see thinglink-2.0 datamodel)
 * 
 */
@MappedSuperclass
public class FeaturedItem extends MetaDataContainer {

    private Boolean _isFeatured;

    private Long _featuredTimeStamp;

    private Integer _weight;

    /**
     * Calls super() to create timeStamp. Initially isFeatured is set false.
     * featuredTimeStamp and weight is initialized 0.
     */
    public FeaturedItem() {
        super();
        _isFeatured = new Boolean(false);
        _featuredTimeStamp = new Long(-1);
        _weight = new Integer(0);
    }

    /**
     * @return True if the item is featured, false otherwise.
     */
    public Boolean isFeatured() {
        return _isFeatured;
    }

    /**
     * @return The featuredTimeStamp is equivalent to the time, when the Item
     *         has been featured.
     */
    public Long getFeaturedTimeStamp() {
        return _featuredTimeStamp;
    }

    /**
     * @return The weight of the featuredItem. If the item is not featured,
     *         weight is -1.
     */
    public Integer getWeight() {
        return _weight;
    }

    /**
     * @param weight
     *            If the item is featured, the weight is set. Otherwise the
     *            weight remains -1.
     */
    public void setWeight(int weight) {
        if (_isFeatured.booleanValue()) {
            _weight = Integer.valueOf(weight);
        }
    }

    /**
     * Sets isFeatured=true and creates a featuredTimeStamp and adds the given
     * weight to the current weight.
     * 
     * @param weight
     *            The weight to add to the featuredItem.
     */
    public void setFeatured(int weight) {
        _isFeatured = Boolean.valueOf(true);
        _featuredTimeStamp = Long.valueOf(System.currentTimeMillis());
        int currentWeight;
        if (_weight != null) {
            currentWeight = _weight.intValue();
        } else {
            currentWeight = 0;
        }
        final int newWeight = currentWeight + weight;
        _weight = Integer.valueOf(newWeight);
    }

    /**
     * Sets isFeatured=false, and featuredTimeStamp and weight -1.
     */
    public void unfeature() {
        _isFeatured = Boolean.valueOf(false);
        _featuredTimeStamp = Long.valueOf(-1);
        _weight = Integer.valueOf(0);
    }

    public void unfeature(int weight) {
        _weight = _weight - weight;
        if (_weight <= 0) {
            _isFeatured = Boolean.valueOf(false);
            _featuredTimeStamp = Long.valueOf(-1);
            _weight = Integer.valueOf(0);
        }
    }
}
