package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.ast.*;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sarath on 06/04/2017.
 */
public class TypeChecker implements IVisitor{

    @Override
    public SymbolInfo visit(NumericExpression expression, Context context) {
        return expression.accept(this, context);
    }

    @Override
    public SymbolInfo visit(UnaryExpression expression, Context context) {
        SymbolInfo symbolInfo = expression.getLeftExpression().accept(this, context);
        Type dataType = symbolInfo.getDataType();
        switch (dataType) {
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
                break;
                default:
                    throw new RuntimeException("Unary operator " + expression.getOperator() + " is not allowed on datatype " + dataType);
        }
        return symbolInfo;
    }

    @Override
    public SymbolInfo visit(ArithmeticExpressionExpression expression, Context context) {
        SymbolInfo lhsInfo = expression.getLeftExpression().accept(this, context);
        SymbolInfo rhsInfo = expression.getRightExpression().accept(this, context);
        return TypeCheckerHelper.checkArithmeticExpresion(lhsInfo, rhsInfo, expression.getOperator());
    }

    @Override
    public SymbolInfo visit(StringLiteral stringLiteral, Context context) {
        return SymbolInfo.builder().withDataType(Type.STRING).build();
    }

    @Override
    public SymbolInfo visit(BooleanExpression booleanExpression, Context context) {
        return SymbolInfo.builder().withDataType(Type.BOOL).build();
    }

    @Override
    public SymbolInfo visit(VariableExpression variableExpression, Context context) {
        return context.getSymbolInfo(variableExpression.getVariableName());
    }

    @Override
    public SymbolInfo visit(RelationalExpression relationalExpression, Context context) {
        SymbolInfo lhsInfo = relationalExpression.getLeftExpression().accept(this, context);
        SymbolInfo rhsInfo = relationalExpression.getRightExpression().accept(this, context);
        return TypeCheckerHelper.checkRelationalExpression(lhsInfo, rhsInfo, relationalExpression.getOperator());
    }

    @Override
    public SymbolInfo visit(LogicalExpression logicalExpression, Context context) {
        SymbolInfo lhsInfo = logicalExpression.getLeftExpression().accept(this, context);
        SymbolInfo rhsInfo = logicalExpression.getRightExpression().accept(this, context);

        Token operator = logicalExpression.getOperator();
        if (lhsInfo.getDataType() == Type.BOOL && rhsInfo.getDataType() == Type.BOOL
                && (operator == Token.OR || operator == Token.ANDAND)){
            return SymbolInfo.builder().withDataType(Type.BOOL).build();
        }
        throw new RuntimeException("Unsupported types lhs : " + lhsInfo.getDataType() + ", rhs : " + rhsInfo.getDataType() + " on operator " + operator);
    }

    @Override
    public SymbolInfo visit(NotExpression notExpression, Context context) {
        SymbolInfo symbolInfo = notExpression.getExpression().accept(this, context);
        if(symbolInfo.getDataType() != Type.BOOL) {
            throw new RuntimeException("Unsupported type " + symbolInfo.getDataType() + " on not operator ");
        }
        return SymbolInfo.builder().withDataType(Type.BOOL).build();
    }

    @Override
    public SymbolInfo visit(PrintStatement printStatement, Context context) {
        printStatement.getExpression().accept(this, context);
        return null;
    }

    @Override
    public SymbolInfo visit(PrintlnStatement printlnStatement, Context context) {
        printlnStatement.accept(this, context);
        return null;
    }

    @Override
    public SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement, Context context) {
        variableDeclarationStatement.getVariableExpression().accept(this, context);
        return null;
    }

    @Override
    public SymbolInfo visit(VariableAssignmentStatement variableAssignmentStatement, Context context) {
        SymbolInfo variableInfo = context.getSymbolInfo(variableAssignmentStatement.getVariableName());
        SymbolInfo rhsExpInfo = variableAssignmentStatement.getExpression().accept(this, context);

        if(null == variableInfo) {
            throw new RuntimeException("Cannot assign to an undefined type");
        } else if(null == variableInfo.getDataType()) {
            return null;
        } else if(variableInfo.getDataType() != rhsExpInfo.getDataType()) {
            throw new RuntimeException("Variable type (" + variableInfo.getDataType() + ") doesn't match the rhs exp type(" + rhsExpInfo.getDataType() + ")");
        }

        return null;

    }

    @Override
    public SymbolInfo visit(VariableDeclAndAssignStatement variableDeclAndAssignStatement, Context context) {
        variableDeclAndAssignStatement.getVariableDeclarationStatement().accept(this, context);
        return variableDeclAndAssignStatement.getVariableAssignmentStatement().accept(this, context);
    }

    @Override
    public SymbolInfo visit(IfStatement ifStatement, Context context) {
        SymbolInfo ifConditionInfo = ifStatement.getBooleanExpression().accept(this, context);
        if(ifConditionInfo.getDataType() != Type.BOOL) {
            throw new RuntimeException("Conditional expressions in if should be of type boolean");
        }

        Context ifContext = new InterpreterContext(context);
        ifContext.setCurrentBlock(ifStatement);

        if(null != ifStatement.getTrueBody()) {
            for(Statement statement : ifStatement.getTrueBody()) {
                if(BreakStatement.class.isAssignableFrom(statement.getClass()) && !context.isInLoopBlock()) {
                    throw new RuntimeException("Break statement is not allowed without a parent loop");
                } else if(ReturnStatement.class.isAssignableFrom(statement.getClass())) {
                    ReturnStatement returnStatement = ReturnStatement.class.cast(statement);
                    SymbolInfo returnInfo = returnStatement.accept(this, context);
                    Function currentFunction = context.getCurrentFunction();
                    if(currentFunction.getReturnType() != returnInfo.getDataType()) {
                        throw new RuntimeException("Return type doesn't (" + currentFunction.getReturnType() + ") match function return type ( " + returnInfo.getDataType() + ")");
                    }
                    //TODO remove unused code - rewrite AST
                    continue;
                }
                statement.accept(this, ifContext);
            }
        }

        if(null != ifStatement.getFalseBody()) {
            for(Statement statement : ifStatement.getFalseBody()) {
                if(BreakStatement.class.isAssignableFrom(statement.getClass()) && !context.isInLoopBlock()) {
                    throw new RuntimeException("Break statement is not allowed without a parent loop");
                } else if(ReturnStatement.class.isAssignableFrom(statement.getClass())) {
                    ReturnStatement returnStatement = ReturnStatement.class.cast(statement);
                    SymbolInfo returnInfo = returnStatement.accept(this, ifContext);
                    Function currentFunction = context.getCurrentFunction();
                    if(currentFunction.getReturnType() != returnInfo.getDataType()) {
                        throw new RuntimeException("Return type doesn't (" + currentFunction.getReturnType() + ") match function return type ( " + returnInfo.getDataType() + ")");
                    }
                    //TODO remove unused code - rewrite AST
                    continue;
                }
                statement.accept(this, ifContext);
            }
        }
        return null;
    }

    @Override
    public SymbolInfo visit(WhileStatement whileStatement, Context context) {
        SymbolInfo conditionInfo = whileStatement.getExpression().accept(this, context);
        if (conditionInfo.getDataType() != Type.BOOL) {
            throw new RuntimeException("Conditional expressions in while should be of type boolean");
        }

        for(Statement statement : whileStatement.getBody()) {
            Context whileContext = new InterpreterContext(context);
            whileContext.setCurrentBlock(whileStatement);
            if(ReturnStatement.class.isAssignableFrom(statement.getClass())) {
                ReturnStatement returnStatement = ReturnStatement.class.cast(statement);
                SymbolInfo returnInfo = returnStatement.accept(this, whileContext);
                Function currentFunction = context.getCurrentFunction();
                if(currentFunction.getReturnType() != returnInfo.getDataType()) {
                    throw new RuntimeException("Return type doesn't (" + currentFunction.getReturnType() + ") match function return type ( " + returnInfo.getDataType() + ")");
                }
                //TODO remove unused code - rewrite AST
                continue;
            }
            statement.accept(this, whileContext);
        }

        return null;
    }

    @Override
    public SymbolInfo visit(BreakStatement breakStatement, Context context) {
        return null;
    }

    @Override
    public SymbolInfo visit(Function function, Context context) {
        return null;
    }

    @Override
    public SymbolInfo visit(ReturnStatement returnStatement, Context context) {
        return returnStatement.getExpression().accept(this, context);
    }

    @Override
    public SymbolInfo visit(FunctionInvokeExpression functionInvokeExpression, Context context) {
        Function function = context.getFunction(functionInvokeExpression.getFunctionName());
        if(function.getFormalArguments().entrySet().size() != functionInvokeExpression.getActualFunctionArguments().size()) {
            throw new RuntimeException("Formal and actual param size doesn't match");
        }

        Iterator<Map.Entry<String, Type>> formalParamEntryIterator = function.getFormalArguments().entrySet().iterator();
        for(Expression expression : functionInvokeExpression.getActualFunctionArguments()) {
            SymbolInfo expressionInfo = expression.accept(this, context);
            Type formalParamtype = formalParamEntryIterator.next().getValue();
            if(formalParamtype != expressionInfo.getDataType()) {
                throw new RuntimeException("Formal and actual param type mismatch");
            }

        }
        return SymbolInfo.builder().withDataType(function.getReturnType()).build();
    }

    @Override
    public SymbolInfo visit(FunctionInvokeStatement functionInvokeStatement, Context context) {
        return functionInvokeStatement.getFunctionInvokeExpression().accept(this, context);
    }

    @Override
    public SymbolInfo visit(VoidExpression voidExpression, Context context) {
        return SymbolInfo.builder().withDataType(Type.VOID).build();
    }
}
