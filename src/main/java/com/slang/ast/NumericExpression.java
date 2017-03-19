package com.slang.ast;

import com.slang.Value;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 16/3/17.
 */
public class NumericExpression extends Expression {

    private double value;

    public NumericExpression(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Value accept(IVisitor visitor) {
        return visitor.visit(this);
    }
}
