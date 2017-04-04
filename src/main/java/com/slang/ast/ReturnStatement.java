package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by Sarath on 03/04/2017.
 */
public class ReturnStatement extends Statement{

    private Expression expression;

    public ReturnStatement(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return "ReturnStatement{" +
                "expression=" + expression +
                '}';
    }
}
