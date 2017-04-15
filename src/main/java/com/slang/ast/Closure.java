package com.slang.ast;

import com.slang.SymbolInfo;

import java.util.Map;

/**
 * Created by Sarath on 15/04/2017.
 */
public class Closure {

    private final Function function;
    private final Map<String, SymbolInfo> capturedVariables;

    public Closure(Function function, Map<String, SymbolInfo> capturedVariables) {
        this.function = function;
        this.capturedVariables = capturedVariables;
    }
}
