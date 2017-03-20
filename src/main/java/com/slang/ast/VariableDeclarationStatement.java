package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 20/3/17.
 */
public class VariableDeclarationStatement extends Statement {

    private VariableExpression variableExpression;

    public VariableDeclarationStatement(VariableExpression variableExpression) {
        this.variableExpression= variableExpression;
    }

    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public VariableExpression getVariableExpression() {
        return variableExpression;
    }
}
