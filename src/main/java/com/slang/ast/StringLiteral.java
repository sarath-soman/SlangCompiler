package com.slang.ast;

import com.slang.Value;
import com.slang.visitor.IVisitor;

/**
 * Created by sarath on 19/3/17.
 */
public class StringLiteral extends Expression {

    private String stringLiteral;

    public Value accept(IVisitor visitor) {
        visitor.visit(this);
        return null;
    }
}
