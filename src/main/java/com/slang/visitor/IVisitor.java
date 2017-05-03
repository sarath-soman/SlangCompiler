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
    SymbolInfo visit(ArithmeticExpressionExpression expression, Context context);
    SymbolInfo visit(StringLiteral stringLiteral, Context context);
    SymbolInfo visit(BooleanExpression booleanExpression, Context context);
    SymbolInfo visit(VariableExpression variableExpression, Context context);
    SymbolInfo visit(ObjectMemberVariableExpression objectMemberVariableExpression, Context context);
    SymbolInfo visit(RelationalExpression relationalExpression, Context context);
    SymbolInfo visit(LogicalExpression logicalExpression, Context context);
    SymbolInfo visit(NotExpression notExpression, Context context);
    SymbolInfo visit(LambdaExpression lambdaExpression, Context context);

    //Statement Visitors
    SymbolInfo visit(PrintStatement printStatement, Context context);
    SymbolInfo visit(PrintlnStatement printlnStatement, Context context);
    SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement, Context context);
    SymbolInfo visit(VariableAssignmentStatement variableAssignmentStatement, Context context);
    SymbolInfo visit(VariableDeclAndAssignStatement variableDeclAndAssignStatement, Context context);
    SymbolInfo visit(IfStatement ifStatement, Context context);
    SymbolInfo visit(WhileStatement whileStatement, Context context);
    SymbolInfo visit(BreakStatement breakStatement, Context context);
    SymbolInfo visit(Function function, Context context);
    SymbolInfo visit(ReturnStatement returnStatement, Context context);
    SymbolInfo visit(FunctionInvokeExpression functionInvokeExpression, Context context);
    SymbolInfo visit(FunctionInvokeStatement functionInvokeStatement, Context context);
    SymbolInfo visit(VoidExpression voidExpression, Context context);
    SymbolInfo visit(Module module, Context context);

}
