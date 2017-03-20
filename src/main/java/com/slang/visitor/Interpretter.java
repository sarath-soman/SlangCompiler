package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public class Interpretter implements IVisitor {

    public SymbolInfo visit(NumericExpression expression, Context context) {
        return new SymbolInfo(expression.getValue());
    }

    public SymbolInfo visit(UnaryExpression expression, Context context) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this, context);
        if(Token.SUB == expression.getOperator()) {
            return new SymbolInfo(leftExpVal.getDoubleValue()* -1);
        }
        return leftExpVal;
    }

    public SymbolInfo visit(BinaryExpression expression, Context context) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this, context);
        SymbolInfo rightExpVal = expression.getRightExpression().accept(this, context);
        Token token = expression.getOperator();
        switch (token) {
            case ADD:
                return new SymbolInfo(leftExpVal.getDoubleValue() + rightExpVal.getDoubleValue());
            case SUB:
                return new SymbolInfo(leftExpVal.getDoubleValue() - rightExpVal.getDoubleValue());
            case DIV:
                return new SymbolInfo(leftExpVal.getDoubleValue() / rightExpVal.getDoubleValue());
            case MUL:
                return new SymbolInfo(leftExpVal.getDoubleValue() * rightExpVal.getDoubleValue());
            default:
                throw new RuntimeException("Unexpected Operator: " + token);
        }
    }

    public SymbolInfo visit(StringLiteral stringLiteral, Context context) {
        return new SymbolInfo(stringLiteral.getStringLiteral());
    }

    public SymbolInfo visit(BooleanExpression booleanExpression, Context context) {
        return new SymbolInfo(booleanExpression.getValue());
    }

    public SymbolInfo visit(VariableExpression variableExpression, Context context) {
        return new SymbolInfo(null, variableExpression.getVariableName());
    }

    public SymbolInfo visit(PrintStatement printStatement, Context context) {
        SymbolInfo exp = printStatement.getExpression().accept(this, context);
        switch (exp.getDataType()) {
            case DOUBLE:
                System.out.print(exp.getDoubleValue());
                break;
            case STRING:
                System.out.print(exp.getStringValue());
                break;
            case BOOL:
                System.out.print(exp.getBoolValue());
                break;

            default:
                throw new RuntimeException("Unknown Data Type");
        }
        return null;
    }

    public SymbolInfo visit(PrintlnStatement printlnStatement, Context context) {
        SymbolInfo exp = printlnStatement.getExpression().accept(this, context);
        switch (exp.getDataType()) {
            case DOUBLE:
                System.out.println(exp.getDoubleValue());
                break;
            case STRING:
                System.out.println(exp.getStringValue());
                break;
            case BOOL:
                System.out.println(exp.getBoolValue());
                break;

            default:
                throw new RuntimeException("Unknown Data Type");
        }
        return null;
    }

    public SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement, Context context) {
        VariableExpression variableExpression= variableDeclarationStatement.getVariableExpression();
        SymbolInfo symbolInfo = variableExpression.accept(this, context);
        SymbolInfo temp = context.getSymbolInfoFromCurrentScope(symbolInfo.getVariableName());
        if(null != temp) {
            throw new RuntimeException("Variable '" + temp.getVariableName() + "' is already defined");
        }
        context.addToSymbolTable(symbolInfo.getVariableName(), symbolInfo);
        return symbolInfo;
    }

    @Override
    public SymbolInfo visit(VariableAssignmentStatement variableAssignmentStatement, Context context) {
        SymbolInfo symbolInfo = context.getSymbolInfo(variableAssignmentStatement.getVariableName());
        if(null == symbolInfo) {
            throw new RuntimeException("Undefined Variable : " + variableAssignmentStatement.getVariableName());
        }
        SymbolInfo valueInfo = variableAssignmentStatement.getExpression().accept(this, context);
        //HERE
        Type dataType = null;
        if(null == valueInfo.getDataType()) {

        }
        switch (valueInfo.getDataType()) {
            case DOUBLE:
                symbolInfo.setDoubleValue(valueInfo.getDoubleValue());
                break;
            case STRING:
                symbolInfo.setStringValue(valueInfo.getStringValue());
                break;
            case BOOL:
                symbolInfo.setBoolValue(valueInfo.getBoolValue());
                break;
        }
        return symbolInfo;
    }

}
