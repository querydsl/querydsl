package com.mysema.query.jpa.domain4;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: nardonep Date: 09/06/11 Time: 13:36 To change
 * this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "BookVersion")
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BookVersion that = (BookVersion) o;

        if (bookID != null ? !bookID.equals(that.bookID) : that.bookID != null)
            return false;
        if (library != null ? !library.equals(that.library)
                : that.library != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bookID != null ? bookID.hashCode() : 0;
        result = 31 * result + (library != null ? library.hashCode() : 0);
        return result;
    }
}