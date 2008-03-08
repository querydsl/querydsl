package com.mysema.query.grammar.hql.domain2;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

/**
 * A thinglink User is represented. (see thinglink-2.0 datamodel)
 * 
 */
@Entity
@Indexed
public class User extends FeaturedItem implements IDisplayObject {

    @Column(unique = true)
    @Field(index = Index.TOKENIZED)
    private String _name;

    @Field(index = Index.TOKENIZED)
    private String _fullName;

    private UserType _type;

    @Column(length = 500)
    private String _password;

    private String _bio;

    @Column(length = 500)
    @Field(index = Index.TOKENIZED)
    private String _homepage;

    @Column(length = 500)
    private String _email;

    @Column(length = 500)
    private String _phone;

    @Column(length = 500)
    private String _flickrUsername;

    @Column(length = 500)
    private String _flickrId;

    @Column(length = 500)
    private String _facebookId;

    @Column(length = 500)
    private String _description;
    
    @Column(length = 500)
    private String _imageFileName;

    private Integer _yearOfBirthday;

    private Integer _monthOfBirthday;

    private Integer _dayOfBirthday;

    @Column(length = 500)
    @Field(index = Index.TOKENIZED)
    private String _originalLocationCountry;

    private Boolean _doChat;

    @Column(length = 500)
    @Field(index = Index.TOKENIZED)
    private String _chatName;

    private ChatType _chatType;

    @Column(length = 500)
    @Field(index = Index.TOKENIZED)
    private String _location;

    @ManyToMany
    private List<User> _friends = new ArrayList<User>();

    @CollectionOfElements
    private List<String> _urls = new ArrayList<String>();

    @Override
    @DocumentId
    public Long getId() {
        return _id;
    }

    public enum UserType {
        ARTIST, COLLECTOR, CRAFTER, CURATOR, DESIGNER, DEVELOPER, ENTREPRENEUR, HOBBYIST, MAGAZINE, MISCELLANEOUS, MUSEUM, RETAILER, STUDENT, TRENDSPOTTER, OTHER, BUSINESS, MANUFACTURER, SCHOOL, TEACHER;
    }

    public enum ChatType {
        NONE, AIM, GOOGLE, ICQ, MSN, YAHOO;
    }

    private boolean _admin = false;

    private String _deactivationId;

    public User() {//
    }

    /**
     * @param name
     */
    public User(String name) {
        super();
        _name = name;
    }

    /**
     * Calls super() to create timeStamp.
     * 
     * @param name
     *            Users name.
     * @param eMail
     *            Users E-Mail.
     */
    public User(String name, String eMail) {
        super();
        _name = name;
        _email = eMail;
    }

    /**
     * Where is the User currently located?
     * 
     * @param location
     */
    public void setLocation(String location) {
        _location = location;
    }

    /**
     * @return Where is the User currently located?
     */
    public String getLocation() {
        return _location;
    }

    /**
     * @param chatType
     */
    public void setChatType(ChatType chatType) {
        _chatType = chatType;
    }

    /**
     * @return
     */
    public ChatType getChatType() {
        return _chatType;
    }

    /**
     * @param chatName
     * @return
     */
    public void setChatName(String chatName) {
        _chatName = chatName;
    }

    /**
     * @return
     */
    public String getChatName() {
        return _chatName;
    }

    /**
     * @param doChat
     */
    public void doChat(Boolean doChat) {
        _doChat = doChat;
    }

    /**
     * @return
     */
    public Boolean doChat() {
        return _doChat;
    }

    /**
     * Where is the User born?
     * 
     * @param country
     */
    public void setOriginalLocationCountry(String country) {
        _originalLocationCountry = country;
    }

    /**
     * @return Where is the User born?
     */
    public String getOriginalLocationCountry() {
        return _originalLocationCountry;
    }

    /**
     * @param fullName
     */
    public void setFullName(String fullName) {
        _fullName = fullName;
    }

    /**
     * @return
     */
    public String getFullName() {
        return _fullName;
    }

    /**
     * @param year
     * @param month
     * @param day
     */
    public void setBirthday(Integer year, Integer month, Integer day) {
        _yearOfBirthday = year;
        _monthOfBirthday = month;
        _dayOfBirthday = day;
    }

    /**
     * @return
     */
    public Integer getYearOfBirthday() {
        return _yearOfBirthday;
    }

    /**
     * @return
     */
    public Integer getMonthOfBirthday() {
        return _monthOfBirthday;
    }

    /**
     * @return
     */
    public Integer getDayOfBirthday() {
        return _dayOfBirthday;
    }

    /**
     * Encrypts the given password.
     * 
     * @param password
     *            Users password.
     */
    public void setPassword(String password) {
        // _password = PasswordUtil.encryptPassword(password);
        _password = password;
    }

    /**
     * @param friend
     *            Another user can be a friend.
     */
    public void addFriend(User friend) {
        _friends.add(friend);
    }

    public boolean removeFriend(User friend) {
        boolean success = _friends.remove(friend);
        return success;
    }

    /**
     * @return All users that are friends of this user. Null if there are no
     *         friends.
     */
    public List<User> getFriends() {
        return _friends;
    }

    /**
     * @param url
     */
    public void addUrl(String url) {
        _urls.add(url);
    }

    /**
     * @return
     */
    public List<String> getUrls() {
        return _urls;
    }

    /**
     * @param name
     *            Name of the user.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * @param type
     */
    public void setType(UserType type) {
        _type = type;
    }

    /**
     * @param email
     *            E-Mail address of the user.
     */
    public void setEmail(String email) {
        _email = email;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    /**
     * @param bio
     *            Users bio.
     */
    public void setBio(String bio) {
        _bio = bio;
    }

    /**
     * @param homepage
     *            Users homepage.
     */
    public void setHomepage(String homepage) {
        _homepage = homepage;
    }

    /**
     * @param flickrUsername
     *            Users flickr username.
     */
    public void setFlickrUsername(String flickrUsername) {
        _flickrUsername = flickrUsername;
    }

    /**
     * @return Users E-Mail.
     */
    public String getEmail() {
        return _email;
    }

    public String getPhone() {
        return _phone;
    }

    /**
     * @return Users bio.
     */
    public String getBio() {
        return _bio;
    }

    /**
     * @return Users homepage.
     */
    public String getHomepage() {
        return _homepage;
    }

    /**
     * @return Users name.
     */
    public String getName() {
        return _name;
    }

    /**
     * @return Users type.
     */
    public UserType getType() {
        return _type;
    }

    /**
     * @return Users flickr username.
     */
    public String getFlickrUsername() {
        return _flickrUsername;
    }

    /**
     * @param imageFileName
     */
    public void setImageFileName(String imageFileName) {
        _imageFileName = imageFileName;
    }

    /**
     * @return
     */
    public String getImageFileName() {
        return _imageFileName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object user) {
        return (((User) user).getName().equals(_name));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return _name + " " + _email;
    }

//    public GrantedAuthority[] getAuthorities() {
//        return new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_USER") };
//    }

    public String getPassword() {
        return _password;
    }

    public String getUsername() {
        return _name;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        return _admin;
    }

    public void setAdmin(boolean admin) {
        _admin = admin;
    }

    public void setDeactivationId(String activationId) {
        _deactivationId = activationId;
    }

    public String getDeactivationId() {
        return _deactivationId;
    }

    public String getFlickrId() {
        return _flickrId;
    }

    public void setFlickrId(String id) {
        _flickrId = id;
    }

    public String getFacebookId() {
        return _facebookId;
    }

    public void setFacebookId(String facebookId) {
        _facebookId = facebookId;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

	public String getDisplayObjectType() {
		return DisplayObjectType.USER.name();
	}

}
