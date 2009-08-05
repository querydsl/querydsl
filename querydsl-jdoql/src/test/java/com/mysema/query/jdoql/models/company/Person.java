package com.mysema.query.jdoql.models.company;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import com.mysema.query.annotations.Entity;

/**
 * Person in a company.
 */
@Entity
public class Person implements Cloneable {
    private long personNum; // Part of PK when app id
    private String globalNum; // Part of PK when app id

    private String firstName;
    private String lastName;
    private String emailAddress;
    private int age;

    private Person bestFriend;

    private Map<String, PhoneNumber> phoneNumbers = new HashMap<String, PhoneNumber>();

    /** Used for the querying of static fields. */
    public static final String FIRSTNAME = "Woody";

    private static Random random = new Random();
    
    public Person() {
    }

    public Person(long num, String first, String last, String email) {
        globalNum = "global:" + Math.abs(random.nextInt());
        personNum = num;
        firstName = first;
        lastName = last;
        emailAddress = email;
    }

    public void setBestFriend(Person p) {
        this.bestFriend = p;
    }

    public Person getBestFriend() {
        return bestFriend;
    }

    public Map<String, PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getGlobalNum() {
        return globalNum;
    }

    public void setGlobalNum(String globalNum) {
        this.globalNum = globalNum;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Object clone() {
        Object o = null;

        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            /* can't happen */
        }

        return o;
    }

    public long getPersonNum() {
        return personNum;
    }

    public void setPersonNum(long num) {
        personNum = num;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String s) {
        firstName = s;
    }

    public synchronized String getLastName() {
        return lastName;
    }

    public void setLastName(String s) {
        lastName = s;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String s) {
        emailAddress = s;
    }

    public boolean compareTo(Object obj) {
        // TODO Use globalNum here too ?
        Person p = (Person) obj;
        return bestFriend == p.bestFriend && firstName.equals(p.firstName)
                && lastName.equals(p.lastName)
                && emailAddress.equals(p.emailAddress)
                && personNum == p.personNum;
    }

    // Note that this is only really correct for application identity, but we
    // also use this class for datastore id
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) personNum;
        hash = 31 * hash + (null == globalNum ? 0 : globalNum.hashCode());
        return hash;
    }

    // Note that this is only really correct for application identity, but we
    // also use this class for datastore id
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((o == null) || (o.getClass() != this.getClass()))
            return false;

        Person other = (Person) o;
        return personNum == other.personNum
                && (globalNum == other.globalNum || (globalNum != null && globalNum
                        .equals(other.globalNum)));
    }

    public String asString() {
        return "Person : number=" + getPersonNum() + " forename="
                + getFirstName() + " surname=" + getLastName() + " email="
                + getEmailAddress() + " bestfriend=" + getBestFriend();
    }

    public static class Id implements Serializable {

        private static final long serialVersionUID = -4893934512712167318L;
        public long personNum;
        public String globalNum;

        public Id() {
        }

        public Id(String str) {
            StringTokenizer toke = new StringTokenizer(str, "::");

            str = toke.nextToken();
            this.personNum = Integer.parseInt(str);
            str = toke.nextToken();
            this.globalNum = str;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof Id)) {
                return false;
            }

            Id c = (Id) obj;
            return personNum == c.personNum && globalNum.equals(c.globalNum);
        }

        public int hashCode() {
            return ((int) this.personNum) ^ this.globalNum.hashCode();
        }

        public String toString() {
            return String.valueOf(this.personNum) + "::"
                    + String.valueOf(this.globalNum);
        }
    }
}