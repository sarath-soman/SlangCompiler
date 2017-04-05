package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by Sarath on 05/04/2017.
 */
public class FunctionInvokeStatement extends Statement {

    private FunctionInvokeExpression functionInvokeExpression;

    public FunctionInvokeStatement(FunctionInvokeExpression functionInvokeExpression) {
        this.functionInvokeExpression = functionInvokeExpression;
    }

    public FunctionInvokeExpression getFunctionInvokeExpression() {
        return functionInvokeExpression;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return "FunctionInvokeStatement{" +
                "functionInvokeExpression=" + functionInvokeExpression +
                '}';
    }
}
