package com.slang;

/**
 * Created by sarath on 19/3/17.
 */
public class SymbolInfo {

    private String variableName;
    private String stringValue;
    private Double doubleValue;
    private Integer integerValue;
    private Boolean boolValue;
    private Type dataType;

    public SymbolInfo(String stringValue) {
        this.stringValue = stringValue;
        dataType = Type.STRING;
    }

    public SymbolInfo(Double doubleValue) {
        this.doubleValue = doubleValue;
        dataType = Type.DOUBLE;
    }

    public SymbolInfo(Integer integerValue) {
        this.integerValue = integerValue;
        dataType = Type.INTEGER;
    }

    public SymbolInfo(Object o, String varName) {
        variableName = varName;
    }

    public SymbolInfo(Boolean boolValue) {
        this.boolValue = boolValue;
        dataType = Type.BOOL;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getStringValue() {
        return stringValue;
    }


    public Double getDoubleValue() {
        return doubleValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public Type getDataType() {
        return dataType;
    }

    public void setStringValue(String stringValue) {
        if (null == dataType || Type.STRING == dataType) {
            this.stringValue = stringValue;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setDoubleValue(Double doubleValue) {
        if (null == dataType || Type.DOUBLE == dataType) {
            this.doubleValue = doubleValue;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setIntegerValue(Integer integerValue) {
        if (null == dataType || Type.INTEGER == dataType) {
            this.integerValue = integerValue;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setBoolValue(Boolean boolValue) {
        if (null == dataType || Type.BOOL == dataType) {
            this.boolValue = boolValue;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }
}
