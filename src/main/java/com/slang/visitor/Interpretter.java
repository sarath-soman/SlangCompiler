package com.slang.visitor;

import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public class Interpretter implements IVisitor {

    public NumericExpression visit(NumericExpression expression) {
        return expression;
    }

    public NumericExpression visit(UnaryExpression expression) {
        NumericExpression leftExp = (NumericExpression) expression.getLeftExpression().accept(this);
        if(Token.SUB == expression.getOperator()) {
            return new NumericExpression(leftExp.getValue() * -1);
        }
        return leftExp;
    }

    public NumericExpression visit(BinaryExpression expression) {
        NumericExpression leftExp = (NumericExpression) expression.getLeftExpression().accept(this);
        NumericExpression rightExp = (NumericExpression) expression.getRightExpression().accept(this);
        Token token = expression.getOperator();
        switch (token) {
            case ADD:
                return new NumericExpression(leftExp.getValue() + rightExp.getValue());
            case SUB:
                return new NumericExpression(leftExp.getValue() - rightExp.getValue());
            case DIV:
                return new NumericExpression(leftExp.getValue() / rightExp.getValue());
            case MUL:
                return new NumericExpression(leftExp.getValue() * rightExp.getValue());
            default:
                throw new RuntimeException("Unexpected Operator: " + token);
        }
    }

    public void visit(PrintStatement printStatement) {
        NumericExpression expression = (NumericExpression) printStatement.getExpression().accept(this);
        System.out.println(expression.getValue());
    }

    public void visit(PrintlnStatement printlnStatement) {
        NumericExpression expression = (NumericExpression) printlnStatement.getExpression().accept(this);
        System.out.println(expression.getValue());
    }
}
