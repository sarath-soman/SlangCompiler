package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 20/3/17.
 */
public class VariableAssignmentStatement extends Statement {

    private String variableName;
    private Expression expression;

    public VariableAssignmentStatement(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public String getVariableName() {
        return variableName;
    }

    public Expression getExpression() {
        return expression;
    }
}
