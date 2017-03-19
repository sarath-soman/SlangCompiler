package com.slang.visitor;

import com.slang.Value;
import com.slang.ast.*;

/**
 * Created by sarath on 18/3/17.
 */
public interface IVisitor {
    Value visit(NumericExpression expression);
    Value visit(UnaryExpression expression);
    Value visit(BinaryExpression expression);
    Value visit(StringLiteral stringLiteral);
    Value visit(PrintStatement printStatement);
    Value visit(PrintlnStatement printlnStatement);

}
