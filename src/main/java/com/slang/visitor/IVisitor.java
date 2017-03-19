package com.slang.visitor;

import com.slang.ValueInfo;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitor {
    ValueInfo visit(NumericExpression expression);
    ValueInfo visit(UnaryExpression expression);
    ValueInfo visit(BinaryExpression expression);
    ValueInfo visit(StringLiteral stringLiteral);
    ValueInfo visit(PrintStatement printStatement);
    ValueInfo visit(PrintlnStatement printlnStatement);

}
