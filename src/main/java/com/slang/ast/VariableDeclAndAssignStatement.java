package com.slang.ast;

import com.slang.SymbolInfo;
import com.slang.visitor.Context;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 20/3/17.
 */
public class VariableDeclAndAssignStatement extends Statement {

    private VariableDeclarationStatement variableDeclarationStatement;
    private VariableAssignmentStatement variableAssignmentStatement;

    public VariableDeclAndAssignStatement(VariableDeclarationStatement variableDeclarationStatement, VariableAssignmentStatement variableAssignmentStatement) {
        this.variableDeclarationStatement = variableDeclarationStatement;
        this.variableAssignmentStatement = variableAssignmentStatement;
    }

    @Override
    public SymbolInfo accept(IVisitor visitor, Context context) {
        return visitor.visit(this, context);
    }

    public VariableDeclarationStatement getVariableDeclarationStatement() {
        return variableDeclarationStatement;
    }

    public VariableAssignmentStatement getVariableAssignmentStatement() {
        return variableAssignmentStatement;
    }

    @Override
    public String toString() {
        return "VariableDeclAndAssignStatement{" +
                "variableDeclarationStatement=" + variableDeclarationStatement +
                ", variableAssignmentStatement=" + variableAssignmentStatement +
                '}';
    }
}
