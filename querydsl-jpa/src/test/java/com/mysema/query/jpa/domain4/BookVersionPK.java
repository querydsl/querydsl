package com.mysema.query.jpa.domain4;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: nardonep Date: 09/06/11 Time: 13:55 To change
 * this template use File | Settings | File Templates.
 */
@Embeddable
public class BookVersionPK implements Serializable {

    private static final long serialVersionUID = 8483495681236266676L;

    private Long bookID;

    private Long library;

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public Long getLibrary() {
        return library;
    }

    public void setLibrary(Long library) {
        this.library = library;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BookVersionPK that = (BookVersionPK) o;

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