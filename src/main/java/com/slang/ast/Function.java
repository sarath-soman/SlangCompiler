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

    private String name;
    private Type returnType;
    private LinkedHashMap<String, Type> formalArguments;
    private List<Statement> body;

    public Function(String name, Type returnType, LinkedHashMap<String, Type> formalArguments, List<Statement> body) {
        this.name = name;
        this.returnType = returnType;
        this.formalArguments = formalArguments;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Map<String, Type> getFormalArguments() {
        return formalArguments;
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
        return "Function{" +
                "name='" + name + '\'' +
                ", returnType=" + returnType +
                ", formalArguments=" + formalArguments +
                ", body=" + body +
                '}';
    }
}
