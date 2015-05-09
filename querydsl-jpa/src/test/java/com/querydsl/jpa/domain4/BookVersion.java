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

import javax.persistence.*;

@Entity
@Table(name = "bookversion_")
@Access(AccessType.PROPERTY)
public class BookVersion implements Serializable {

    private static final long serialVersionUID = -1697470794339057030L;

    private BookID bookID;

    private BookVersionPK pk = new BookVersionPK();

    private Library library;

    private BookDefinition definition;

    @EmbeddedId
    public BookVersionPK getPk() {
        return pk;
    }

    public void setPk(BookVersionPK pk) {
        this.pk = pk;
    }

    @MapsId("bookID")
    @ManyToOne(cascade = CascadeType.ALL)
    public BookID getBookID() {
        return bookID;
    }

    public void setBookID(BookID bookID) {
        this.bookID = bookID;
    }

    @MapsId("library")
    @ManyToOne(cascade = CascadeType.ALL)
    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    @Embedded
    public BookDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(BookDefinition definition) {
        this.definition = definition;
    }

}