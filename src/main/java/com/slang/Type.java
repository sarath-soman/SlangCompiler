package com.slang;

import java.util.LinkedHashMap;

/**
 * Created by sarath on 19/3/17.
 */
public class Type {

    public static final Type STRING = new Type("STRING");
    public static final Type FLOAT = new Type("FLOAT");
    public static final Type INTEGER = new Type("INTEGER");
    public static final Type DOUBLE = new Type("DOUBLE");
    public static final Type BOOL = new Type("BOOL");
    public static final Type LONG = new Type("LONG");
    public static final Type VOID = new Type("VOID");
    public static final Type FUNCTION = new Type();

    private String typeName;

    public Type() {
    }

    public Type(String typeName) {
        this.typeName = typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return "Type{" +
                "typeName='" + typeName + '\'' +
                '}';
    }
}
