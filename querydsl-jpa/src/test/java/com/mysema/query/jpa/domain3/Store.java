package com.mysema.query.jpa.domain3;

import java.io.Serializable;

public class Store implements Serializable {

    private static final long serialVersionUID = 7221730732392000227L;

    private String code;

    private String name;

    private String address;

    private String city;

    private String phoneDetails;

    private String faxDetails;

    private String zipCode;

    private String chainCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneDetails() {
        return phoneDetails;
    }

    public void setPhoneDetails(String phoneDetails) {
        this.phoneDetails = phoneDetails;
    }

    public String getFaxDetails() {
        return faxDetails;
    }

    public void setFaxDetails(String faxDetails) {
        this.faxDetails = faxDetails;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

}