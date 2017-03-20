package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public class Interpretter implements IVisitor {

    public SymbolInfo visit(NumericExpression expression) {
        return new SymbolInfo(expression.getValue());
    }

    public SymbolInfo visit(UnaryExpression expression) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this);
        if(Token.SUB == expression.getOperator()) {
            return new SymbolInfo(leftExpVal.getDoubleValue()* -1);
        }
        return leftExpVal;
    }

    public SymbolInfo visit(BinaryExpression expression) {
        SymbolInfo leftExpVal = expression.getLeftExpression().accept(this);
        SymbolInfo rightExpVal = expression.getRightExpression().accept(this);
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

    public SymbolInfo visit(StringLiteral stringLiteral) {
        return new SymbolInfo(stringLiteral.getStringLiteral());
    }

    public SymbolInfo visit(BooleanExpression booleanExpression) {
        return new SymbolInfo(booleanExpression.getValue());
    }

    public SymbolInfo visit(PrintStatement printStatement) {
        SymbolInfo exp = printStatement.getExpression().accept(this);
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

    public SymbolInfo visit(PrintlnStatement printlnStatement) {
        SymbolInfo exp = printlnStatement.getExpression().accept(this);
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

    public SymbolInfo visit(VariableDeclarationStatement variableDeclarationStatement) {
        System.out.println(variableDeclarationStatement.getVariableInfo());
        return variableDeclarationStatement.getVariableInfo();
    }

}
