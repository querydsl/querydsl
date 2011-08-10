package com.mysema.query.jpa.domain4;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

/**
 * Created by IntelliJ IDEA. User: nardonep Date: 09/08/11 Time: 12:24 To change
 * this template use File | Settings | File Templates.
 */
@Embeddable
public class BookMark {

    private Long page;

    private String comment;

    @Basic
    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    @Basic
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BookMark bookMark = (BookMark) o;

        if (page != null ? !page.equals(bookMark.page) : bookMark.page != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return page != null ? page.hashCode() : 0;
    }
}
