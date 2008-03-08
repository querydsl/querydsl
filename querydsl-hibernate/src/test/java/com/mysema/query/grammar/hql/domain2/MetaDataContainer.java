package com.mysema.query.grammar.hql.domain2;

import java.util.HashMap;

import javax.persistence.MappedSuperclass;

/**
 * Objects can have metadata as key/value pairs. Internally those pairs are
 * stored in a HashMap. (see thinglink-2.0 datamodel)
 * 
 * 
 */
@MappedSuperclass
public class MetaDataContainer extends TimeStamped {

    private HashMap<String, String> _metaDataEntries;

    /**
     * Calls super() to create timeStamp.
     */
    public MetaDataContainer() {
        super();
    }

    /**
     * Add a metadata entry to this container.
     * 
     * @param key
     *            Metadata key.
     * @param value
     *            Metadata value.
     */
    public void addMetaDataEntry(String key, String value) {
        if (_metaDataEntries == null)
            _metaDataEntries = new HashMap<String, String>(10);
        _metaDataEntries.put(key, value);
    }

    /**
     * Retrieve a metadata entry by key.
     * 
     * @param key
     *            The key of the metadata entry to retrieve.
     * @return The value of the metadata entry.
     */
    public String getMetaDataEntry(String key) {
        if (_metaDataEntries != null)
            return _metaDataEntries.get(key);
        return null;
    }

}
