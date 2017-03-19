package com.slang.visitor;

import com.slang.ValueInfo;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public class TypeChecker implements IVisitor {

    public ValueInfo visit(NumericExpression expression) {
        return new ValueInfo(expression.getValue());
    }

    public ValueInfo visit(UnaryExpression expression) {
        ValueInfo leftExpVal = expression.getLeftExpression().accept(this);
        if(Token.SUB == expression.getOperator()) {
            return new ValueInfo(leftExpVal.getDoubleValue()* -1);
        }
        return leftExpVal;
    }

    public ValueInfo visit(BinaryExpression expression) {
        ValueInfo leftExpVal = expression.getLeftExpression().accept(this);
        ValueInfo rightExpVal = expression.getRightExpression().accept(this);
        Token token = expression.getOperator();
        switch (token) {
            case ADD:
                return new ValueInfo(leftExpVal.getDoubleValue() + rightExpVal.getDoubleValue());
            case SUB:
                return new ValueInfo(leftExpVal.getDoubleValue() - rightExpVal.getDoubleValue());
            case DIV:
                return new ValueInfo(leftExpVal.getDoubleValue() / rightExpVal.getDoubleValue());
            case MUL:
                return new ValueInfo(leftExpVal.getDoubleValue() * rightExpVal.getDoubleValue());
            default:
                throw new RuntimeException("Unexpected Operator: " + token);
        }
    }

    public ValueInfo visit(StringLiteral stringLiteral) {
        return null;
    }

    public ValueInfo visit(PrintStatement printStatement) {
        ValueInfo exp = printStatement.getExpression().accept(this);
        System.out.print(exp.getDoubleValue());
        return null;
    }

    public ValueInfo visit(PrintlnStatement printlnStatement) {
        ValueInfo exp = printlnStatement.getExpression().accept(this);
        System.out.println(exp.getDoubleValue());
        return null;
    }
}
