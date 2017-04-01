package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

import java.util.List;

/**
 * Created by Sarath on 01/04/2017.
 */
public class WhileStatement extends Statement {

    private Expression expression;

    private List<Statement> body;

    public WhileStatement(Expression expression, List<Statement> body) {
        this.expression = expression;
        this.body = body;
    }

    public Expression getExpression() {
        return expression;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return "WhileStatement{" +
                "expression=" + expression +
                ", body=" + body +
                '}';
    }
}
