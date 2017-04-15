package com.slang;

import com.slang.ast.Function;

/**
 * Created by sarath on 19/3/17.
 */
public class SymbolInfo {

    private String variableName;
    private String stringValue;
    private Float floatValue;
    private Double doubleValue;
    private Integer integerValue;
    private Long longValue;
    private Boolean boolValue;
    private Function functionValue;
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

    public SymbolInfo(Float floatValue) {
        this.floatValue = floatValue;
        dataType = Type.FLOAT;
    }

    public SymbolInfo(Long longValue) {
        this.longValue = longValue;
        dataType = Type.LONG;
    }

    public SymbolInfo(Object o, String varName) {
        variableName = varName;
    }

    public SymbolInfo(Boolean boolValue) {
        this.boolValue = boolValue;
        dataType = Type.BOOL;
    }

    public SymbolInfo(Function functionValue) {
        this.functionValue = functionValue;
        dataType = Type.FUNCTION;
    }

    public SymbolInfo() {

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

    public Float getFloatValue() {
        return floatValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public Type getDataType() {
        return dataType;
    }

    public Function getFunctionValue() {
        return functionValue;
    }

    public void setStringValue(String stringValue) {
        if (null == dataType || Type.STRING == dataType) {
            this.stringValue = stringValue;
            dataType = Type.STRING;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setFloatValue(Float floatValue) {
        if (null == dataType || Type.FLOAT == dataType) {
            this.floatValue = floatValue;
            dataType = Type.FLOAT;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setDoubleValue(Double doubleValue) {
        if (null == dataType || Type.DOUBLE == dataType) {
            this.doubleValue = doubleValue;
            dataType = Type.DOUBLE;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setIntegerValue(Integer integerValue) {
        if (null == dataType || Type.INTEGER == dataType) {
            this.integerValue = integerValue;
            dataType = Type.INTEGER;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setLongValue(Long longValue) {
        if (null == dataType || Type.LONG == dataType) {
            this.longValue = longValue;
            dataType = Type.LONG;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setBoolValue(Boolean boolValue) {
        if (null == dataType || Type.BOOL == dataType) {
            this.boolValue = boolValue;
            dataType = Type.BOOL;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void setFunctionValue(Function functionValue) {
        if (null == dataType || Type.FUNCTION == dataType) {
            this.functionValue = functionValue;
            dataType = Type.FUNCTION;
        } else {
            throw new RuntimeException("Type mismatch on assigning the value");
        }
    }

    public void nullify() {
        stringValue = null;
        floatValue = null;
        doubleValue = null;
        integerValue = null;
        longValue = null;
        boolValue = null;
        functionValue = null;
    }

    @Override
    public String toString() {
        return "SymbolInfo{" +
                "variableName='" + variableName + '\'' +
                ", stringValue='" + stringValue + '\'' +
                ", floatValue=" + floatValue +
                ", doubleValue=" + doubleValue +
                ", integerValue=" + integerValue +
                ", longValue=" + longValue +
                ", boolValue=" + boolValue +
                ", functionValue=" + functionValue +
                ", dataType=" + dataType +
                '}';
    }


    public static Builder builder() {
        return new Builder();
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public static final class Builder {
        private SymbolInfo symbolInfo = new SymbolInfo();

        private Builder() {
        }


        public Builder withVariableName(String variableName) {
            symbolInfo.variableName = variableName;
            return this;
        }

        public Builder withStringValue(String stringValue) {
            symbolInfo.stringValue = stringValue;
            return this;
        }

        public Builder withFloatValue(Float floatValue) {
            symbolInfo.floatValue = floatValue;
            return this;
        }

        public Builder withDoubleValue(Double doubleValue) {
            symbolInfo.doubleValue = doubleValue;
            return this;
        }

        public Builder withIntegerValue(Integer integerValue) {
            symbolInfo.integerValue = integerValue;
            return this;
        }

        public Builder withLongValue(Long longValue) {
            symbolInfo.longValue = longValue;
            return this;
        }

        public Builder withBoolValue(Boolean boolValue) {
            symbolInfo.boolValue = boolValue;
            return this;
        }

        public Builder withFunctionValue(Function boolValue) {
            symbolInfo.functionValue = boolValue;
            return this;
        }

        public Builder withDataType(Type dataType) {
            symbolInfo.dataType = dataType;
            return this;
        }

        public SymbolInfo build() {
            return symbolInfo;
        }
    }
}
