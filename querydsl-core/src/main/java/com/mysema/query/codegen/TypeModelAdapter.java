package com.mysema.query.codegen;

public class TypeModelAdapter implements TypeModel{
    
    private final TypeModel typeModel;
    
    public TypeModelAdapter(TypeModel typeModel){
        this.typeModel = typeModel;
    }

    public TypeModel as(TypeCategory category) {
        return typeModel.as(category);
    }

    public TypeModel asAnySubtype() {
        return typeModel.asAnySubtype();
    }

    public String getFullName() {
        return typeModel.getFullName();
    }

    public String getLocalGenericName(BeanModel context) {
        return typeModel.getLocalGenericName(context);
    }

    public String getLocalRawName(BeanModel context) {
        return typeModel.getLocalRawName(context);
    }

    public String getPackageName() {
        return typeModel.getPackageName();
    }

    public TypeModel getParameter(int i) {
        return typeModel.getParameter(i);
    }

    public int getParameterCount() {
        return typeModel.getParameterCount();
    }

    public String getPrimitiveName() {
        return typeModel.getPrimitiveName();
    }

    public TypeModel getSelfOrValueType() {
        return typeModel.getSelfOrValueType();
    }

    public String getSimpleName() {
        return typeModel.getSimpleName();
    }

    public TypeCategory getTypeCategory() {
        return typeModel.getTypeCategory();
    }

    public boolean isExtendsType() {
        return typeModel.isExtendsType();
    }

    public boolean isFinal() {
        return typeModel.isFinal();
    }

    public boolean isPrimitive() {
        return typeModel.isPrimitive();
    }

    public String toString() {
        return typeModel.toString();
    }

}
