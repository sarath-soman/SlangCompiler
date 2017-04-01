package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitable;
import com.slang.visitor.IVisitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sarath on 01/04/2017.
 */
public class Function implements IVisitable {

    private Type returnType;
    private LinkedHashMap<String, Type> formalArguments;
    private Expression returnExpression;
    private List<Statement> body;

    public Function(Type returnType, LinkedHashMap<String, Type> formalArguments, Expression returnExpression, List<Statement> body) {
        this.returnType = returnType;
        this.formalArguments = formalArguments;
        this.returnExpression = returnExpression;
        this.body = body;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Map<String, Type> getFormalArguments() {
        return formalArguments;
    }

    public Expression getReturnExpression() {
        return returnExpression;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return null;
    }
}
