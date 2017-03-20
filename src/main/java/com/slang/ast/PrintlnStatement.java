package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 18/3/17.
 */
public class PrintlnStatement extends Statement {

    private Expression expression;

    public PrintlnStatement(Expression expression) {
        this.expression = expression;
    }

    public SymbolInfo accept(IVisitor visitor, Context context) {
        visitor.visit(this, context);
        return null;
    }

    public Expression getExpression() {
        return expression;
    }
}
