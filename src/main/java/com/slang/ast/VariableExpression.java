package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 20/3/17.
 */
public class VariableExpression extends Expression {
    private final String variableName;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return "VariableExpression{" +
                "variableName='" + variableName + '\'' +
                '}';
    }
}
