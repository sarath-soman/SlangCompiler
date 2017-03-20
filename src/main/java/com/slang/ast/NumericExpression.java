package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
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

    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }
}
