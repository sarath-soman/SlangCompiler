package com.slang.visitor;

import com.slang.ast.BinaryExpression;
import com.slang.ast.NumericExpression;
import com.slang.ast.UnaryExpression;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitor {
    NumericExpression visit(NumericExpression expression);
    NumericExpression visit(UnaryExpression expression);
    NumericExpression visit(BinaryExpression expression);
}
