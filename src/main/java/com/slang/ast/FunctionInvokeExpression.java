package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

import java.util.List;

/**
 * Created by Sarath on 03/04/2017.
 */
public class FunctionInvokeExpression extends Expression {

    private String functionName;
    private List<Expression> actualFunctionArguments;

    public FunctionInvokeExpression(String functionName, List<Expression> actualFunctionArguments) {
        this.functionName = functionName;
        this.actualFunctionArguments = actualFunctionArguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Expression> getActualFunctionArguments() {
        return actualFunctionArguments;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    @Override
    public String toString() {
        return "FunctionInvokeExpression{" +
                "functionName='" + functionName + '\'' +
                ", actualFunctionArguments=" + actualFunctionArguments +
                '}';
    }
}
