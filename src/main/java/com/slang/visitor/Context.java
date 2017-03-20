package com.slang.visitor;

import com.slang.SymbolInfo;

/**
 * Created by sarath on 20/3/17.
 */
public abstract class Context {

    public abstract void addToSymbolTable(String symbolName, SymbolInfo symbolInfo);

    public abstract SymbolInfo getSymbolInfo(String symbolName);

    public abstract SymbolInfo getSymbolInfoFromCurrentScope(String symbolName);

}
