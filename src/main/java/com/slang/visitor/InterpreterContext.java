package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.ast.Function;
import com.slang.ast.Statement;
import com.slang.ast.WhileStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sarath on 20/3/17.
 */
public class InterpreterContext extends Context {

    protected Context parentContext = null;
    protected Map<String, Function> functionTable;
    protected Function currentFunction;
    protected Statement currentBlock; //Can be 'if' or 'while'

    public InterpreterContext(Map<String, Function> functions) {
        this.functionTable = functions;
    }

    public InterpreterContext(Context parentContext, Map<String, Function> functionTable) {
        this.parentContext = parentContext;
        this.functionTable = functionTable;
    }

    public static enum Scope {
        CURRENT,
        ALL
    }

    private Map<String, SymbolInfo> symbolTable = new HashMap<>();

    public InterpreterContext(Context parentContext) {
        this.parentContext = parentContext;
    }

    public InterpreterContext() {
        super();
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

    @Override
    public Function getFunction(String functionIdentifier) {
        return null == functionTable
                ? null == parentContext
                ? null : parentContext.getFunction(functionIdentifier)
                : functionTable.get(functionIdentifier);
    }

    @Override
    public void setCurrentFunction(Function function) {
        currentFunction = function;
    }

    @Override
    public Function getCurrentFunction() {
        return currentFunction;
    }

    @Override
    public void setCurrentBlock(Statement statement) {
        currentBlock = statement;
    }

    @Override
    public boolean isInLoopBlock() {
        return null == currentBlock ? false : WhileStatement.class.isAssignableFrom(currentBlock.getClass())
                ? true : null == parentContext ? false : parentContext.isInLoopBlock();
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
