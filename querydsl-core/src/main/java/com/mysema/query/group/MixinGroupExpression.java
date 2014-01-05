package com.mysema.query.group;

public class MixinGroupExpression<E, F, R> extends AbstractGroupExpression<E, R> {
    
    private static final long serialVersionUID = -5419707469727395643L;

    private class GroupCollectorImpl implements GroupCollector<E, R> {
       
        private final GroupCollector<F, R> mixinGroupCollector;
        
        private GroupCollector<E, F> groupCollector;
        
        public GroupCollectorImpl() {
            mixinGroupCollector = mixin.createGroupCollector();
        }
        
        public void add(E input) {
            if (groupCollector == null) {
                groupCollector = groupExpression.createGroupCollector();
            }
            groupCollector.add(input);
        }
        
        
        @Override
        public R get() {
            if (groupCollector != null) {
                F output = groupCollector.get();
                mixinGroupCollector.add(output);
                groupCollector = null;
            }
            return mixinGroupCollector.get();
        }

    }

    private final GroupExpression<F, R> mixin;
    
    private final GroupExpression<E, F> groupExpression;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MixinGroupExpression(GroupExpression<E, F> groupExpression, GroupExpression<F, R> mixin) {
        super((Class) mixin.getType(), groupExpression.getExpression());
        this.mixin = mixin;
        this.groupExpression = groupExpression;
    }

    @Override
    public GroupCollector<E, R> createGroupCollector() {
        return new GroupCollectorImpl();
    } 
    
}
