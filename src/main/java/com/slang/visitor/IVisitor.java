package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitor {
    //Expression Visitors
    SymbolInfo visit(NumericExpression expression);
    SymbolInfo visit(UnaryExpression expression);
    SymbolInfo visit(BinaryExpression expression);
    SymbolInfo visit(StringLiteral stringLiteral);
    SymbolInfo visit(BooleanExpression booleanExpression);

    //Statement Visitors
    SymbolInfo visit(PrintStatement printStatement);
    SymbolInfo visit(PrintlnStatement printlnStatement);
    SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement);
}
