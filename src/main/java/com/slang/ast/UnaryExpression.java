package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
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

    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return "UnaryExpression{" +
                "leftExpression=" + leftExpression +
                ", operator=" + operator +
                '}';
    }
}
