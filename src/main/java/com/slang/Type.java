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

    private final String typeName;
    private final TypeCategory typeCategory;

    public Type(String typeName, TypeCategory typeCategory) {
        this.typeName = typeName;
        this.typeCategory = typeCategory;
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
                ", typeCategory=" + typeCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (typeName != null ? !typeName.equals(type.typeName) : type.typeName != null) return false;
        return typeCategory == type.typeCategory;
    }

    @Override
    public int hashCode() {
        int result = typeName != null ? typeName.hashCode() : 0;
        result = 31 * result + (typeCategory != null ? typeCategory.hashCode() : 0);
        return result;
    }
}
