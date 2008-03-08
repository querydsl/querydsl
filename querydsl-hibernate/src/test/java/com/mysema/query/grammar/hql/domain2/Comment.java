/**
 * 
 */
package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A comment is Created by a User and belongs to a Thing. A Thing has a List
 * with its comments. (see thinglink-2.0 datamodel)
 * 
 * 
 */
@Entity
public class Comment extends TimeStamped {

    @Column(length = 500)
    private String _userName;

    @Column(length = 500)
    private String _userEmail;

    @Column(length = 15000)
    private String _text;

    private Boolean _isNew;

    public Comment() {
    }

    /**
     * Calls super() to create timeStamp. _isNew is initialized with true.
     * 
     * @param userName
     *            Name of the user who created the Comment.
     * @param text
     *            The comment.
     */
    public Comment(String userName, String text) {
        super();
        _userName = userName;
        _text = text;
        _isNew = new Boolean(true);
    }

    /**
     * @return Name of the user who created the Comment.
     */
    public String getUserName() {
        return _userName;
    }

    public void setUserEmail(String userEmail) {
        _userEmail = userEmail;
    }

    /**
     * @return EMail of the user who created the Comment.
     */
    public String getUserEmail() {
        return _userEmail;
    }

    /**
     * @return Text of the Comment.
     */
    public String getText() {
        return _text;
    }

    /**
     * @param text
     *            Text of the Comment.
     */
    public void setText(String text) {
        _text = text;
    }

    /**
     * Call this method, when the User has read the Comment.
     */
    public void setOld() {
        _isNew = new Boolean(false);
    }

    /**
     * @return False, when the User has read the Comment.
     */
    public Boolean isNew() {
        return _isNew;
    }

    @Override
    public boolean equals(Object comment) {
        return ((((Comment) comment).getUserName().equals(_userName)) && (((Comment) comment).getText().equals(_text)) && (((Comment) comment)
                .getTimeStamp().equals(getTimeStamp())));
    }
}
