package com.slang.visitor;

import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitor {
    NumericExpression visit(NumericExpression expression);
    NumericExpression visit(UnaryExpression expression);
    NumericExpression visit(BinaryExpression expression);

    void visit(PrintStatement printStatement);
    void visit(PrintlnStatement printlnStatement);
}
