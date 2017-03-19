package com.slang.ast;

import com.slang.Value;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 18/3/17.
 */
public class PrintlnStatement extends Statement {

    private Expression expression;

    public PrintlnStatement(Expression expression) {
        this.expression = expression;
    }

    public Value accept(IVisitor visitor) {
        visitor.visit(this);
        return null;
    }

    public Expression getExpression() {
        return expression;
    }
}
