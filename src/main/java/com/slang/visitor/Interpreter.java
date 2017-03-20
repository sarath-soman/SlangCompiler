package com.slang.visitor;

import com.slang.SymbolInfo;
import com.slang.Type;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public class Interpreter implements IVisitor {

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
        System.out.println(leftExpVal);
        System.out.println(rightExpVal);
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
        return context.getSymbolInfo(variableExpression.getVariableName());
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
        String variableToBeDeclared = variableExpression.getVariableName();
        SymbolInfo temp = context.getSymbolInfoFromCurrentScope(variableToBeDeclared);
        if(null != temp) {
            throw new RuntimeException("Variable '" + temp.getVariableName() + "' is already defined");
        }
        SymbolInfo decSymbolInfo = new SymbolInfo(null, variableToBeDeclared);
        context.addToSymbolTable(variableToBeDeclared, decSymbolInfo);
        return decSymbolInfo;
    }

    public SymbolInfo visit(VariableAssignmentStatement variableAssignmentStatement, Context context) {
        //LHS
        SymbolInfo lhsInfo = context.getSymbolInfo(variableAssignmentStatement.getVariableName());
        if(null == lhsInfo) {
            throw new RuntimeException("Undefined Variable : " + variableAssignmentStatement.getVariableName());
        }
        Type lhsType = lhsInfo.getDataType();

        //RHS
        SymbolInfo rhsInfo = variableAssignmentStatement.getExpression().accept(this, context);
        if(null == rhsInfo) {
            throw new RuntimeException("Undefined Variable : " +
                    VariableExpression.class.cast(variableAssignmentStatement.getExpression()).getVariableName());
        }
        Type rhsType = rhsInfo.getDataType();
        if(null == lhsType && null != rhsType) {
            //when lhs is declared and rhs has value
            switch (rhsInfo.getDataType()) {
                case DOUBLE:
                    lhsInfo.setDoubleValue(rhsInfo.getDoubleValue());
                    break;
                case STRING:
                    lhsInfo.setStringValue(rhsInfo.getStringValue());
                    break;
                case BOOL:
                    lhsInfo.setBoolValue(rhsInfo.getBoolValue());
                    break;
            }
        } else if(null != lhsType && null == rhsType) {
            //assigning already declared but not assigned variable to lhs
            lhsInfo.nullify();
        } else if(null != lhsInfo && null != rhsInfo) {
            //mutating lhs with value of rhs
            if(lhsType == Type.DOUBLE && rhsType == Type.DOUBLE) {
                lhsInfo.setDoubleValue(rhsInfo.getDoubleValue());
            } else if(lhsType == Type.DOUBLE && rhsType == Type.INTEGER) {
                lhsInfo.setDoubleValue(Double.valueOf(rhsInfo.getIntegerValue()));
            } else if(lhsType == Type.INTEGER && rhsType == Type.INTEGER) {
                lhsInfo.setIntegerValue(rhsInfo.getIntegerValue());
            } else if(lhsType == Type.BOOL && rhsType == Type.BOOL) {
                lhsInfo.setBoolValue(rhsInfo.getBoolValue());
            } else if(lhsType == Type.STRING && rhsType == Type.STRING) {
                lhsInfo.setStringValue(rhsInfo.getStringValue());
            } else {
                throw new RuntimeException("Unsupported types lhs : " + lhsType + ", rhs : " + rhsType);
            }

        }
        //if lhs is just declared and rhs is also just declared then do nothing
        return lhsInfo;
    }

    @Override
    public SymbolInfo visit(VariableDeclAndAssignStatement variableDeclAndAssignStatement, Context context) {
        variableDeclAndAssignStatement.getVariableDeclarationStatement().accept(this, context);
        return variableDeclAndAssignStatement.getVariableAssignmentStatement().accept(this, context);
    }

}
