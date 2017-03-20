package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 20/3/17.
 */
public class VariableDeclarationStatement extends Statement {

    private SymbolInfo variableInfo;

    public VariableDeclarationStatement(SymbolInfo variableInfo) {
        this.variableInfo = variableInfo;
    }

    public SymbolInfo accept(IVisitor visitor) {
        return visitor.visit(this);
    }

    public SymbolInfo getVariableInfo() {
        return variableInfo;
    }
}
