package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.ast.Function;
import com.slang.ast.Statement;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sarath on 20/3/17.
 */
public abstract class Context {

    public abstract void addToSymbolTable(String symbolName, SymbolInfo symbolInfo);

    public abstract SymbolInfo getSymbolInfo(String symbolName);

    public abstract SymbolInfo getSymbolInfoFromCurrentScope(String symbolName);

    public abstract Function getFunction(String functionIdentifier);

    public abstract void setCurrentFunction(Function function);

    public abstract Function getCurrentFunction();

    public abstract void setCurrentBlock(Statement statement);

    public abstract boolean isInLoopBlock() ;

}
