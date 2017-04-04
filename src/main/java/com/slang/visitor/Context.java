package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.ast.Function;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sarath on 20/3/17.
 */
public abstract class Context {

    public Context() {
    }

    public Context(Map<String, Function> functionTable) {
        this.functionTable = functionTable;
    }

    private Map<String, Function> functionTable = new LinkedHashMap<>();

    public abstract void addToSymbolTable(String symbolName, SymbolInfo symbolInfo);

    public abstract SymbolInfo getSymbolInfo(String symbolName);

    public abstract SymbolInfo getSymbolInfoFromCurrentScope(String symbolName);

    public void addToFunctionTable(String functionIdentifier, Function function) {
        functionTable.put(functionIdentifier, function);
    }

    public Function getFunction(String functionIdentifier) {
        return functionTable.get(functionIdentifier);
    }

}
