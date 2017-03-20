package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 20/3/17.
 */
public class BooleanExpression extends Expression {

    private Boolean value;

    public BooleanExpression(Boolean value) {
        this.value = value;
    }

    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BooleanExpression{" +
                "value=" + value +
                '}';
    }
}
