package com.mysema.query.collections;

/**
 * AliasFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public class AliasFactory {
    
    public <A> A createAlias(Class<A> cl, String var){
        try {
            // TODO : create real alias
            return cl.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("error", e);
        }
    }

}
