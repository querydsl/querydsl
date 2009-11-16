package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public class TypeSuperModel extends TypeModelAdapter{
    
    private static final TypeModel objectModel = new ClassTypeModel(TypeCategory.SIMPLE, Object.class);
    
    private TypeModel superModel;
    
    public TypeSuperModel(TypeModel typeModel) {
        super(objectModel);
        this.superModel = typeModel;
    }

    @Override
    public StringBuilder getLocalGenericName(BeanModel context, StringBuilder builder, boolean asArgType) {
        if (!asArgType){
            builder.append("? super ");
            return superModel.getLocalGenericName(context, builder, true);
        }else{
            return super.getLocalGenericName(context, builder, asArgType);
        }    
    }

}
