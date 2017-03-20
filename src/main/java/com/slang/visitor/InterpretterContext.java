package com.slang.visitor;

import com.slang.SymbolInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sarath on 20/3/17.
 */
public class InterpretterContext extends Context {

    public static enum Scope {
        CURRENT,
        ALL
    }

    private Map<String, SymbolInfo> symbolTable = new HashMap<>();

    private Context parentContext = null;

    public InterpretterContext(Context parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public void addToSymbolTable(String symbolName, SymbolInfo symbolInfo) {
        symbolTable.put(symbolName, symbolInfo);
    }

    @Override
    public SymbolInfo getSymbolInfo(String symbolName) {
        SymbolInfo symbolInfo = symbolTable.get(symbolName);
        return null != symbolInfo ? symbolInfo
                : null == parentContext ? null
                : parentContext.getSymbolInfo(symbolName);
    }



    @Override
    public SymbolInfo getSymbolInfoFromCurrentScope(String symbolName) {
        return symbolTable.get(symbolName);
    }

    public SymbolInfo getSymbolInfo(String symbolName, Scope scope) {
        switch (scope) {
            case ALL:
                return getSymbolInfo(symbolName);
            case CURRENT:
                return getSymbolInfoFromCurrentScope(symbolName);

            default:
                throw new RuntimeException("Unsupported scope value");

        }
    }
}
