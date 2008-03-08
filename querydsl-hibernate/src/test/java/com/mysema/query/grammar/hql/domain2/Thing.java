/**
 * 
 */
package com.mysema.query.grammar.hql.domain2;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

/**
 * A thinglink Thing is represented. This is the core of the data model. (see
 * thinglink-2.0 datamodel)
 * 
 */
@Entity
@Indexed
public class Thing extends FeaturedItem implements IDisplayObject {

    @Column(unique = true)
    private String _code;

    @CollectionOfElements
    private List<String> _imageFileNames = new ArrayList<String>();

    @CollectionOfElements
    private List<String> _soundFileNames = new ArrayList<String>();

    @OneToMany(cascade = { CascadeType.ALL })
    private List<Comment> _comments = new ArrayList<Comment>();

    @ManyToMany(cascade = { CascadeType.ALL })
    private List<Maker> _makers = new ArrayList<Maker>();

    @ManyToMany(cascade = { CascadeType.ALL })
//    @Field(index = Index.TOKENIZED)
//    @FieldBridge(impl = TagsListBridge.class)
    private List<Tag> _tags = new ArrayList<Tag>();

    @ManyToMany(cascade = { CascadeType.ALL })
//    @Field(index = Index.TOKENIZED)
//    @FieldBridge(impl = CountriesListBridge.class)
    private List<Country> _countries = new ArrayList<Country>();

    @ManyToMany(cascade = { CascadeType.ALL })
    private List<Year> _years = new ArrayList<Year>();

    @ManyToOne
    private User _linker;

    @ManyToMany(cascade = { CascadeType.ALL })
    private List<Location> _locations = new ArrayList<Location>();

    @ManyToOne(cascade = { CascadeType.ALL })
//    @Field(index = Index.TOKENIZED)
//    @FieldBridge(impl = LocationBridge.class)
    private Location _currentLocation;

    @Override
    @DocumentId
    public Long getId() {
        return _id;
    }

    private ThingPrivacy _privacyLevel;

    @Field(index = Index.TOKENIZED)
    private String _name;

    @Column(length = 500)
    private String _password;

    private Boolean _isHidden;

    @Column(length = 40000)
    @Field(index = Index.TOKENIZED)
    private String _description;

    private Integer _viewCount;

    private Boolean _isVersion1;

    @Column(length = 500)
    private String _imageFileName;

    private Integer _createYear;

    private Integer _createMonth;

    private Integer _createDay;

    public enum ThingPrivacy {
        PRIVATE, FRIENDS, PUBLIC;
    }

    public Thing() {//
    }

    /**
     * Initially the Thing is not hidden.
     * 
     * @param code
     *            The unique code of the Thing. Something like "ABC123".
     */
    public Thing(String code) {
        _code = code;
        _isHidden = new Boolean(false);
        _isVersion1 = new Boolean(false);
    }

    /**
     * @param code
     *            The unique code of the Thing.
     */
    public void setCode(String code) {
        _code = code;
    }

    /**
     * @return The unique code of the Thing.
     */
    public String getCode() {
        return _code;
    }

    /**
     * Set true if this Thing has been created with thinglink-1.0
     * 
     * @param version1
     */
    public void setVersion1(boolean version1) {
        _isVersion1 = new Boolean(version1);
    }

    /**
     * @return True if this Thing has been created with thinglink-1.0
     */
    public Boolean isVersion1() {
        return _isVersion1;
    }

    /**
     * @param linker
     *            The User who created the Thing.
     */
    public void setLinker(User linker) {
        _linker = linker;
    }

    /**
     * @return The User who created the Thing.
     */
    public User getLinker() {
        return _linker;
    }

    /**
     * Should be called each time the Thing is viewed.
     */
    public void view() {
        if (_viewCount == null)
            _viewCount = new Integer(0);
        _viewCount = Integer.valueOf(_viewCount.intValue() + 1);
    }

    public void setViewCount(int count) {
        _viewCount = new Integer(count);
    }

    /**
     * @return Viewcount for the Thing.
     */
    public Integer getViewCount() {
        return _viewCount;
    }

    /**
     * @param comment
     *            The Comment to add to the Thing.
     */
    public void addComment(Comment comment) {
        _comments.add(comment);
    }

    /**
     * @return All Comments that belong to this Thing.
     */
    public List<Comment> getComments() {
        return _comments;
    }

    /**
     * @param imageFileName
     */
    public void addImageFileName(String imageFileName) {
        if (_imageFileNames == null)
            _imageFileNames = new ArrayList<String>();
        _imageFileNames.add(imageFileName);
    }

    public void addSoundFileName(String soundFileName) {
        if (_soundFileNames == null)
            _soundFileNames = new ArrayList<String>();
        _soundFileNames.add(soundFileName);
    }

    /**
     * @return
     */
    public List<String> getImageFileNames() {
        return _imageFileNames;
    }

    public List<String> getSoundFileNames() {
        return _soundFileNames;
    }

    /**
     * Adds a Location to the Thing. The last Location added represents the
     * current Location of the Thing.
     * 
     * @param location
     *            The current Location of the Thing.
     */
    public void addLocation(Location location) {
        _locations.add(location);
        _currentLocation = location;
    }

    /**
     * @return All Locations where the Thing has been located during the past.
     */
    public List<Location> getLocations() {
        return _locations;
    }

    /**
     * @return The Location where the Thing is currently located.
     */
    public Location getCurrentLocation() {
        // return _locations.get(_locations.size()-1);
        return _currentLocation;
    }

    /**
     * @param name
     *            Name of the Maker of this Thing.
     */
    public void addMaker(Maker maker) {
        if (_makers == null)
            _makers = new ArrayList<Maker>();
        if (_makers.contains(maker))
            _makers.remove(maker);
        _makers.add(0, maker);
    }

    /**
     * @return All Makers of this Thing.
     */
    public List<Maker> getMakers() {
        return _makers;
    }

    /**
     * @param keyword
     *            Keyword of the Tag.
     */
    public void addTag(Tag tag) {
        if (_tags == null)
            _tags = new ArrayList<Tag>();
        if (!_tags.contains(tag))
            _tags.add(tag);
    }

    /**
     * @return All Tags of this Thing.
     */
    public List<Tag> getTags() {
        return _tags;
    }

    /**
     * @param name
     *            Name of the Country.
     */
    public void addCountry(Country country) {
        if (_countries == null)
            _countries = new ArrayList<Country>();
        if (_countries.contains(country))
            _countries.remove(country);
        _countries.add(0, country);
    }

    /**
     * @return All Countries this Thing belongs to.
     */
    public List<Country> getCountries() {
        return _countries;
    }

    /**
     * @param year
     */
    public void addYear(Year year) {
        if (_years == null)
            _years = new ArrayList<Year>();
        if (!_years.contains(year))
            _years.add(year);
    }

    /**
     * @return
     */
    public List<Year> getYears() {
        return _years;
    }

    /**
     * @param privacyLevel
     *            The privacy level of the Thing.
     */
    public void setPrivacyLevel(ThingPrivacy privacy) {
        _privacyLevel = privacy;
    }

    /**
     * @return Privacy level of the Thing.
     */
    public ThingPrivacy getPrivacyLevel() {
        return _privacyLevel;
    }

    /**
     * @param name
     *            Name of the Thing.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * @return Name of the Thing.
     */
    public String getName() {
        if (_name == null)
            return "unknown name";
        return _name;
    }

    /**
     * Encrypts the given password.
     * 
     * @param password
     *            Things password.
     */
    public void setPassword(String password) {
//        _password = PasswordUtil.encryptPassword(password);
        _password = password;
    }

    /**
     * Tests whether the given password is equal to Things password.
     * 
     * @param password
     *            The non-encrypted password to test.
     * @return True if passwords are equal.
     */
    public boolean equalsEncryptedPassword(String password) {
//        return _password.equals(PasswordUtil.encryptPassword(password));
        return _password.equals(password);
    }

    /**
     * @param hidden
     */
    public void setHidden(boolean hidden) {
        _isHidden = Boolean.valueOf(hidden);
    }

    /**
     * @return
     */
    public Boolean isHidden() {
        return _isHidden;
    }

    /**
     * @return The description of the Thing.
     */
    public String getDescription() {
        if (_description == null)
            return "";
        return _description;
    }

    /**
     * @param description
     *            The description of the Thing.
     */
    public void setDescription(String description) {
        _description = description;
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

    public Integer getCreateDay() {
        return _createDay;
    }

    public void setCreateDay(Integer createDay) {
        _createDay = createDay;
    }

    public Integer getCreateMonth() {
        return _createMonth;
    }

    public void setCreateMonth(Integer createMonth) {
        _createMonth = createMonth;
    }

    public Integer getCreateYear() {
        return _createYear;
    }

    public void setCreateYear(Integer createYear) {
        _createYear = createYear;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object thing) {
        return ((Thing) thing).getCode().equals(_code);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return _code;
    }

    public void removeTags() {
        _tags = new ArrayList<Tag>();
    }

    public void deleteImages(int[] toDeleteImageIndices) {
        for (int i = toDeleteImageIndices.length - 1; i > -1; i--) {
            _imageFileNames.remove(toDeleteImageIndices[i]);
        }
    }

    public void deleteSounds(int[] toDeleteSoundIndices) {
        for (int i = toDeleteSoundIndices.length - 1; i > -1; i--) {
            _soundFileNames.remove(toDeleteSoundIndices[i]);
        }
    }

	public String getDisplayObjectType() {
		return DisplayObjectType.THING.name();
	}

}
