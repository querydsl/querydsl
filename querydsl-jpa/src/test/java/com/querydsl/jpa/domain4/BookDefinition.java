/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.jpa.domain4;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@Embeddable
@Access(AccessType.PROPERTY)
public class BookDefinition implements Serializable {

    private static final long serialVersionUID = 3570098308959717614L;

    private String name;

    private String description;

    private List<BookMark> bookMarks;

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ElementCollection()
    @CollectionTable(name = "book_bookmarks")
    @OrderColumn()
    public List<BookMark> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(List<BookMark> bookMarks) {
        this.bookMarks = bookMarks;
    }

}