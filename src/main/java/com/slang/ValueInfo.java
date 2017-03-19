package com.slang;

/**
 * Created by sarath on 19/3/17.
 */
public class ValueInfo {

    private String stringValue;
    private Double doubleValue;
    private Integer integerValue;
    private Type dataType;

    public ValueInfo(String stringValue) {
        this.stringValue = stringValue;
        dataType = Type.STRING;
    }

    public ValueInfo(Double doubleValue) {
        this.doubleValue = doubleValue;
        dataType = Type.DOUBLE;
    }

    public ValueInfo(Integer integerValue) {
        this.integerValue = integerValue;
        dataType = Type.INTEGER;
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

    public Type getDataType() {
        return dataType;
    }
}
