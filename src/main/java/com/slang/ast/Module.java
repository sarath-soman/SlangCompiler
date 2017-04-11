package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitable;
import com.slang.visitor.IVisitor;

import java.util.Map;

/**
 * Created by Sarath on 11/04/2017.
 */
public class Module implements IVisitable {

    private Map<String, Function> functionsMap;

    public Module(Map<String, Function> functionsMap) {
        this.functionsMap = functionsMap;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public Map<String, Function> getFunctionsMap() {
        return functionsMap;
    }

    @Override
    public String toString() {
        return "Module{" +
                "functionsMap=" + functionsMap +
                '}';
    }
}
