package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

import java.util.List;

/**
 * Created by Sarath on 31/03/2017.
 */
public class IfStatement extends Statement {

    private Expression booleanExpression;

    private List<Statement> body;

    public IfStatement(Expression booleanExpression, List<Statement> body) {
        this.booleanExpression = booleanExpression;
        this.body = body;
    }

    public Expression getBooleanExpression() {
        return booleanExpression;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }
}
