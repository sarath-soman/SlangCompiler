package com.slang.ast;

import com.slang.Value;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 16/3/17.
 */
public class UnaryExpression extends Expression {

    private Expression leftExpression;
    private Token operator;

    public UnaryExpression(Expression leftExpression, Token operator) {
        this.leftExpression = leftExpression;
        this.operator = operator;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Token getOperator() {
        return operator;
    }

    public Value accept(IVisitor visitor) {
        return visitor.visit(this);
    }
}
