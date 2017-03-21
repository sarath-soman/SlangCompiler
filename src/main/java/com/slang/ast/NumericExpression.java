package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 16/3/17.
 */
public class NumericExpression extends Expression {

    private Double doubleValue;
    private Float floatValue;
    private Long longValue;
    private Integer integerValue;
    private Type dataType;

    public NumericExpression(Double doubleValue) {
        this.doubleValue = doubleValue;
        dataType = Type.DOUBLE;
    }

    public NumericExpression(Float floatValue) {
        this.floatValue = floatValue;
        dataType = Type.FLOAT;
    }

    public NumericExpression(Long longValue) {
        this.longValue = longValue;
        dataType = Type.LONG;
    }

    public NumericExpression(Integer integerValue) {
        this.integerValue = integerValue;
        dataType = Type.INTEGER;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public Type getDataType() {
        return dataType;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return "NumericExpression{" +
                "doubleValue=" + doubleValue +
                ", floatValue=" + floatValue +
                ", longValue=" + longValue +
                ", integerValue=" + integerValue +
                ", dataType=" + dataType +
                '}';
    }
}
