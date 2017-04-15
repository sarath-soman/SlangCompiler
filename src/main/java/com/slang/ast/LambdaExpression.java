package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

import java.util.List;
import java.util.Map;

/**
 * Created by Sarath on 15/04/2017.
 */
public class LambdaExpression extends Expression {

    final private List<String> capturedVariables;
    final private Function function;

    public LambdaExpression(Function function) {
        this.function = function;
        capturedVariables = null;
    }

    public LambdaExpression(List<String> capturedVariables, Function function) {
        this.capturedVariables = capturedVariables;
        this.function = function;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public List<String> getCapturedVariables() {
        return capturedVariables;
    }

    public Function getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return "LambdaExpression{" +
                "capturedVariables=" + capturedVariables +
                ", function=" + function +
                '}';
    }
}
