package com.querydsl.maven;

import com.querydsl.sql.Configuration;

/**
 * Specifies mapping customization options.
 *
 * @author tiwe
 */
public interface Mapping {

    /**
     * Apply the customization to the passed in configuration.
     *
     * @param configuration the configuration to apply the customization to
     */
    void apply(Configuration configuration);

}
