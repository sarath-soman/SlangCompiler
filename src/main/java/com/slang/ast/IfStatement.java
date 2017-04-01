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

    private List<Statement> trueBody;
    private List<Statement> falseBody;

    public IfStatement(Expression booleanExpression, List<Statement> trueBody, List<Statement> falseBody) {
        this.booleanExpression = booleanExpression;
        this.trueBody = trueBody;
        this.falseBody = falseBody;
    }

    public Expression getBooleanExpression() {
        return booleanExpression;
    }

    public List<Statement> getTrueBody() {
        return trueBody;
    }

    public List<Statement> getFalseBody() {
        return falseBody;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }
}
