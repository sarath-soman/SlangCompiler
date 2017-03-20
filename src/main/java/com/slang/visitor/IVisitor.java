package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitor {
    //Expression Visitors
    SymbolInfo visit(NumericExpression expression, Context context);
    SymbolInfo visit(UnaryExpression expression, Context context);
    SymbolInfo visit(BinaryExpression expression, Context context);
    SymbolInfo visit(StringLiteral stringLiteral, Context context);
    SymbolInfo visit(BooleanExpression booleanExpression, Context context);
    SymbolInfo visit(VariableExpression variableExpression, Context context);

    //Statement Visitors
    SymbolInfo visit(PrintStatement printStatement, Context context);
    SymbolInfo visit(PrintlnStatement printlnStatement, Context context);
    SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement, Context context);
    SymbolInfo visit(VariableAssignmentStatement variableAssignmentStatement, Context context);
    SymbolInfo visit(VariableDeclAndAssignStatement variableDeclAndAssignStatement, Context context);
}
