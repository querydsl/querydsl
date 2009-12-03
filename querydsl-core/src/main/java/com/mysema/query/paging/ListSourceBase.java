package com.mysema.query.paging;

import java.util.Collections;
import java.util.List;

/**
 * ListSourceBase provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class ListSourceBase<T> implements ListSource<T>{
    
    private final CallbackService callbackService;
    
    private final long size;
    
    public ListSourceBase(CallbackService callbackService, long size){
        this.callbackService = callbackService;
        this.size = size;
    }
    
    public static <T> ListSource<T> emptyResults() {
        return new ListSource<T>(){
            @Override
            public List<T> getResults(int fromIndex, int toIndex) {
                return Collections.emptyList();
            }
            @Override
            public boolean isEmpty() {
                return true;
            }
            @Override
            public long size() {
                return 0l;
            }                        
        };
    }
    
    protected abstract List<T> getInnerResults(int fromIndex, int toIndex);
    
    public List<T> getResults(final int fromIndex, final int toIndex){
        return callbackService.invoke(new Callback<List<T>>(){
            @Override
            public List<T> invoke() {
                return getInnerResults(fromIndex, toIndex);
            }            
        });
    }
    
    public final boolean isEmpty(){
        return size == 0l;
    }

    public final long size(){
        return size;
    }

}
