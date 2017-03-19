package com.slang.visitor;

import com.slang.Value;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public class Interpretter implements IVisitor {

    public Value visit(NumericExpression expression) {
        return new Value(expression.getValue());
    }

    public Value visit(UnaryExpression expression) {
        Value leftExpVal = expression.getLeftExpression().accept(this);
        if(Token.SUB == expression.getOperator()) {
            return new Value(leftExpVal.getDoubleValue()* -1);
        }
        return leftExpVal;
    }

    public Value visit(BinaryExpression expression) {
        Value leftExpVal = expression.getLeftExpression().accept(this);
        Value rightExpVal = expression.getRightExpression().accept(this);
        Token token = expression.getOperator();
        switch (token) {
            case ADD:
                return new Value(leftExpVal.getDoubleValue() + rightExpVal.getDoubleValue());
            case SUB:
                return new Value(leftExpVal.getDoubleValue() - rightExpVal.getDoubleValue());
            case DIV:
                return new Value(leftExpVal.getDoubleValue() / rightExpVal.getDoubleValue());
            case MUL:
                return new Value(leftExpVal.getDoubleValue() * rightExpVal.getDoubleValue());
            default:
                throw new RuntimeException("Unexpected Operator: " + token);
        }
    }

    public Value visit(StringLiteral stringLiteral) {
        return null;
    }

    public Value visit(PrintStatement printStatement) {
        Value exp = printStatement.getExpression().accept(this);
        System.out.print(exp.getDoubleValue());
        return null;
    }

    public Value visit(PrintlnStatement printlnStatement) {
        Value exp = printlnStatement.getExpression().accept(this);
        System.out.println(exp.getDoubleValue());
        return null;
    }
}
