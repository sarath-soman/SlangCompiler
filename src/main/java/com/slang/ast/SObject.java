package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.Type;
import java.util.Map;

/**
 * Created by sarath on 26/04/17.
 */
public class SObject {

    private Map<String, SymbolInfo> fields;

    private Map<String, Function> functions;

    private Type type;

    public Map<String, SymbolInfo> getFields() {
        return fields;
    }

    public SymbolInfo getField(String fieldName) {
        return fields.get(fieldName);
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }

    public Function getFunction(String functionName) {
        return functions.get(functionName);
    }

    public void addField(String name, SymbolInfo symbolInfo) {
        fields.put(name, symbolInfo);
    }

    public void addFunction(String name, Function function) {
        functions.put(name, function);
    }

    public Type getType() {
        return type;
    }
}
