package com.slang;

import java.util.LinkedHashMap;

/**
 * Created by sarath on 19/3/17.
 */
public class Type{

    public static final Type STRING = new Type("STRING", TypeCategory.PRIMITIVE);
    public static final Type FLOAT = new Type("FLOAT", TypeCategory.PRIMITIVE);
    public static final Type INTEGER = new Type("INTEGER", TypeCategory.PRIMITIVE);
    public static final Type DOUBLE = new Type("DOUBLE", TypeCategory.PRIMITIVE);
    public static final Type BOOL = new Type("BOOL", TypeCategory.PRIMITIVE);
    public static final Type LONG = new Type("LONG", TypeCategory.PRIMITIVE);
    public static final Type VOID = new Type("VOID", TypeCategory.PRIMITIVE);

    private String typeName;
    private TypeCategory typeCategory;

    public Type(String typeName, TypeCategory typeCategory) {
        this.typeName = typeName;
        this.typeCategory = typeCategory;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public TypeCategory getTypeCategory() {
        return typeCategory;
    }

    @Override
    public String toString() {
        return "Type{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}
