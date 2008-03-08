package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * The Location specifies where a Thing is currently located, whereas a
 * {@link Country} specifies where a Thing comes from.
 */
@Entity
public class Location extends TimeStamped {

    @Column(length = 500)
    private String _country;

    @Column(length = 500)
    private String _city;

    @Column(length = 500)
    private String _zipCode;

    private Double _longitude;

    private Double _latitude;

    @Column(length = 500)
    private String _street;

    @Column(length = 500)
    private String _url;

    public Location() {//
    }

    /**
     * @param country
     * @param url
     * @param zipCode
     */
    public Location(String country, String url, String zipCode) {
        super();
        _country = country;
        _url = url;
        _zipCode = zipCode;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        _country = country;
    }

    /**
     * @return The country where the Thing is currently located.
     */
    public String getCountry() {
        return _country;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        _city = city;
    }

    /**
     * @return The city where the Thing is currently located.
     */
    public String getCity() {
        return _city;
    }

    /**
     * @param zipCode
     */
    public void setZipCode(String zipCode) {
        _zipCode = zipCode;
    }

    /**
     * @return The zip code where the Thing is currently located.
     */
    public String getZipCode() {
        return _zipCode;
    }

    /**
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        _longitude = longitude;
    }

    /**
     * @return
     */
    public Double getLongitude() {
        return _longitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        _latitude = latitude;
    }

    /**
     * @param latitude
     * @return
     */
    public Double getLatitude(Double latitude) {
        return _latitude;
    }

    /**
     * @param street
     */
    public void setStreet(String street) {
        _street = street;
    }

    /**
     * @return
     */
    public String getStreet() {
        return _street;
    }

    /**
     * @param url
     *            This could be a URL like
     *            http://beta.plazes.com/plaze/f93d8d1a909b6f80341e5b4f9178b218
     */
    public void setUrl(String url) {
        _url = url;
    }

    /**
     * @return A URL like
     *         http://beta.plazes.com/plaze/f93d8d1a909b6f80341e5b4f9178b218 if
     *         previously set.
     */
    public String getUrl() {
        return _url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object location) {
        if (location == null)
            return false;

        return (((Location) location).getUrl().equals(_url) && ((Location) location).getZipCode().equals(_zipCode) && ((Location) location)
                .getCountry().equals(_country));
    }
}
