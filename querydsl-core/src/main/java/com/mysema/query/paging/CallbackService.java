package com.mysema.query.paging;

/**
 * CallbackService provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface CallbackService {
    
    <RT> RT invoke(Callback<RT> cb);

}
