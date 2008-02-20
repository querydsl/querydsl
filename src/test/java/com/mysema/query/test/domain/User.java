package com.mysema.query.test.domain;

import com.mysema.query.grammar.Types.NumberProperty;
import com.mysema.query.grammar.Types.StringProperty;
import com.mysema.query.test.domain.Domain.qCompany;

/**
 * User provides
 *
 * @author tiwe
 * @version $Id$
 */
public class User {
//    private Company company;
//    public final NumberProperty id = num("id");
//    public final StringProperty userName = str("userName");
//    public final StringProperty firstName = str("firstName");
//    public final StringProperty lastName = str("lastName");
    private qCompany company;
    private long id;
    private String userName, firstName, lastName;
    public qCompany getCompany() {
        return company;
    }
    public void setCompany(qCompany company) {
        this.company = company;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
}
