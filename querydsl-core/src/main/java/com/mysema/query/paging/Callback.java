package com.mysema.query.paging;

/**
 * Callback provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Callback<RT> {
    
    RT invoke();

}
