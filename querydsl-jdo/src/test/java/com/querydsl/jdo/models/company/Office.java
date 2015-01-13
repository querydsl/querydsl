/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jdo.models.company;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import com.querydsl.core.annotations.QueryEntity;

/**
 * An office in the company.
 */
@QueryEntity
public class Office {
    private long floor; // PK when app-id
    private String roomName; // PK when app-id

    private String description;
    private Set<Department> departments = new HashSet<Department>(); // 1-N uni
    // relation
    // using
    // join
    // table
    private Date date;

    public Office() {
    }

    public Office(long floor, String roomName, String description) {
        this.floor = floor;
        this.roomName = roomName;
        this.description = description;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    /**
     * Accessor for the roomt name
     *
     * @return Returns the room name.
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Mutator for the room name
     *
     * @param roomName
     *            The room name
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getFloor() {
        return floor;
    }

    public void setFloor(long floor) {
        this.floor = floor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String s) {
        description = s;
    }

    public void addDepartment(Department dept) {
        departments.add(dept);
    }

    public void clearDepartments() {
        departments.clear();
    }

    // Note that this is only really correct for application identity, but we
    // also use this class for datastore id
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) floor;
        hash = 31 * hash + (null == roomName ? 0 : roomName.hashCode());
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

        Office other = (Office) o;
        return floor == other.floor
                && (roomName == other.roomName || (roomName != null && roomName.equals(other.roomName)));
    }

    public String asString() {
        return "Office : floor=" + getFloor() + ", room=" + getRoomName()
                + "- " + getDescription();
    }

    public static class Id implements Serializable {

        private static final long serialVersionUID = -4032898077139179659L;
        public long floor;
        public String roomName;

        public Id() {
        }

        /**
         * String constructor.
         */
        public Id(String str) {
            StringTokenizer toke = new StringTokenizer(str, "::");

            str = toke.nextToken();
            this.floor = Integer.parseInt(str);
            str = toke.nextToken();
            this.roomName = str;
        }

        /**
         * Implementation of equals method.
         */
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof Id)) {
                return false;
            }

            Id c = (Id) obj;

            return floor == c.floor && roomName.equals(c.roomName);
        }

        /**
         * Implementation of hashCode method that supports the equals-hashCode
         * contract.
         */
        public int hashCode() {
            return ((int) this.floor) ^ this.roomName.hashCode();
        }

        /**
         * Implementation of toString that outputs this object id's primary key
         * values.
         */
        public String toString() {
            return String.valueOf(this.floor) + "::"
                    + String.valueOf(this.roomName);
        }
    }
}
